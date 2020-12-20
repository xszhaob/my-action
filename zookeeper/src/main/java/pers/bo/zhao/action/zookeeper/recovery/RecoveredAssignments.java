package pers.bo.zhao.action.zookeeper.recovery;

import org.apache.zookeeper.*;

import java.util.ArrayList;
import java.util.List;

public class RecoveredAssignments {

    /**
     * 所有的任务
     */
    private List<String> tasks;
    /**
     * 已经分配的任务
     */
    private List<String> assignments;
    private List<String> status;
    private List<String> activeWorkers;
    private List<String> assignedWorkers;

    private RecoveryCallback callback;
    private ZooKeeper zk;


    /**
     * 回调函数。
     * 恢复后或恢复失败后的回调。
     */
    public interface RecoveryCallback {
        int OK = 0;
        int FAILED = -1;

        void recoveryComplete(int rc, List<String> tasks);
    }


    public RecoveredAssignments(ZooKeeper zk) {
        this.zk = zk;
        this.assignments = new ArrayList<>();
    }

    public void recovery(RecoveryCallback recoveryCallback) {
        callback = recoveryCallback;
        getTasks();
    }

    private void getTasks() {
        zk.getChildren("/tasks", false, tasksCallback, null);
    }

    private AsyncCallback.ChildrenCallback tasksCallback = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getTasks();

                    break;
                case OK:
                    System.out.println("Getting tasks for recovery");
                    tasks = children;
                    getAssignedWorkers();

                    break;
                default:
                    System.out.println("getChildren failed" + KeeperException.create(KeeperException.Code.get(rc), path));
                    callback.recoveryComplete(RecoveryCallback.FAILED, null);
            }
        }
    };

    private void getAssignedWorkers() {
        zk.getChildren("/assign", false, assignedWorkersCallback, null);
    }

    private AsyncCallback.ChildrenCallback assignedWorkersCallback = (rc, path, ctx, children) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                getAssignedWorkers();
                break;
            case OK:
                assignedWorkers = children;
                getWorkers();
                break;
            default:
                System.out.println("getChildren failed" + KeeperException.create(KeeperException.Code.get(rc), path));
                callback.recoveryComplete(RecoveryCallback.FAILED, null);
        }
    };

    /**
     * @param ctx 已经分配任务的worker节点。/assign/的子节点信息。
     */
    private void getWorkers() {
        zk.getChildren("/workers", false, workersCallback, null);
    }

    private AsyncCallback.ChildrenCallback workersCallback = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getWorkers();
                    break;
                case OK:
                    System.out.println("Getting worker assignments for recovery: " + children.size());
                    if (children.size() == 0) {
                        System.out.println("Empty list of workers, possibly just starting");
                        callback.recoveryComplete(RecoveryCallback.OK, new ArrayList<String>());
                        break;
                    }

                    activeWorkers = children;

                    for (String assignedWorker : assignedWorkers) {
                        getWorkerAssignments("/assign/" + assignedWorker);
                    }
            }
        }
    };

    /**
     * @param assignWorker /assign/worker-*
     */
    private void getWorkerAssignments(String assignWorker) {
        zk.getChildren(assignWorker, false, workerAssignmentsCallback, null);
    }

    private AsyncCallback.ChildrenCallback workerAssignmentsCallback = (rc, path, ctx, children) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                getWorkerAssignments(path);
                break;
            case OK:
                String worker = path.replace("/assign/", "");

                // 活跃中的worker
                if (activeWorkers.contains(worker)) {
                    assignments.addAll(children);
                } else {
                    for (String task : children) {
                        if (!tasks.contains(task)) {
                            tasks.add(task);
                            getDataReassign(path, task);
                        } else {
                            deleteAssignment(path + "/" + task);
                        }
                    }
                    deleteAssignment(path);
                }

                assignedWorkers.remove(worker);

                if (assignedWorkers.size() == 0) {
                    System.out.println("Getting statuses for recovery");
                    getStatuses();
                }
        }
    };

    private void getDataReassign(String path, String task) {
        zk.getData(path,
                false,
                getDataReassignCallback,
                task);
    }

    class RecreateTaskCtx {
        String path;
        String task;
        byte[] data;

        RecreateTaskCtx(String path, String task, byte[] data) {
            this.path = path;
            this.task = task;
            this.data = data;
        }
    }

    private AsyncCallback.DataCallback getDataReassignCallback = (rc, path, ctx, data, stat) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                getDataReassign(path, (String) ctx);

                break;
            case OK:
                recreateTask(new RecreateTaskCtx(path, (String) ctx, data));

                break;
            default:
                System.out.println("Something went wrong when getting data " +
                        KeeperException.create(KeeperException.Code.get(rc)));
        }
    };


    private void recreateTask(RecreateTaskCtx ctx) {
        zk.create("/tasks/" + ctx.task,
                ctx.data,
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT,
                recreateTaskCallback,
                ctx);
    }


    private AsyncCallback.StringCallback recreateTaskCallback = (rc, path, ctx, name) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                recreateTask((RecreateTaskCtx) ctx);

                break;
            case OK:
                deleteAssignment(((RecreateTaskCtx) ctx).path);

                break;
            case NODEEXISTS:
                System.out.println("Node shouldn't exist: " + path);

                break;
            default:
                System.out.println("Something went wrong when recreating task" +
                        KeeperException.create(KeeperException.Code.get(rc)));
        }
    };


    private void deleteAssignment(String path) {
        zk.delete(path, -1, taskDeletionCallback, null);
    }

    private AsyncCallback.VoidCallback taskDeletionCallback = (rc, path, rtx) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                deleteAssignment(path);
                break;
            case OK:
                System.out.println("Task correctly deleted: " + path);
                break;
            default:
                System.out.println("Failed to delete task data" +
                        KeeperException.create(KeeperException.Code.get(rc), path));
        }
    };


    private void getStatuses() {
        zk.getChildren("/status", false, statusCallback, null);
    }


    private AsyncCallback.ChildrenCallback statusCallback = new AsyncCallback.ChildrenCallback() {
        public void processResult(int rc,
                                  String path,
                                  Object ctx,
                                  List<String> children) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getStatuses();

                    break;
                case OK:
                    System.out.println("Processing assignments for recovery");
                    status = children;
                    processAssignments();

                    break;
                default:
                    System.out.println("getChildren failed" + KeeperException.create(KeeperException.Code.get(rc), path));
                    callback.recoveryComplete(RecoveryCallback.FAILED, null);
            }
        }
    };

    private void processAssignments() {
        System.out.println("Size of tasks: " + tasks.size());

        // 分配过的任务
        for (String assignment : assignments) {
            deleteAssignment("/tasks/" + assignment);
            tasks.remove(assignment);
        }

        System.out.println("Size of tasks after assignment filtering: " + tasks.size());
        for (String s : status) {
            deleteAssignment("/tasks/" + s);
            tasks.remove(s);
        }

        System.out.println("Size of tasks after status filtering: " + tasks.size());
        callback.recoveryComplete(RecoveryCallback.OK, tasks);
    }
}
