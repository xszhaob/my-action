package pers.bo.zhao.action.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import pers.bo.zhao.action.zookeeper.recovery.RecoveredAssignments;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Master implements Watcher {


    private ZooKeeper zk;
    private String hostPort;
    private String serverId = Integer.toHexString(new Random().nextInt());
    private boolean isLeader = false;
    private volatile MasterStates state = MasterStates.RUNNING;
    private volatile boolean connected = false;
    private volatile boolean expired = false;
    private static final Random RANDOM = new Random();

    private ChildrenCache workersCache;
    private ChildrenCache tasksCache;


    public Master(String hostPort) {
        this.hostPort = hostPort;
    }

    public void startZk() {
        try {
            this.zk = new ZooKeeper(hostPort, 15000, this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void stopZk() {
        try {
            if (zk != null) {
                zk.close();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.None) {
            switch (watchedEvent.getState()) {
                case SyncConnected:
                    connected = true;
                    break;
                case Disconnected:
                    connected = false;
                    break;
                case Expired:
                    expired = true;
                    connected = false;
                    System.out.println("Session expiration");
                    break;
                default:
                    break;
            }
        }
    }


    void bootstrap() {
        createParent("/workers", new byte[0]);
        createParent("/assign", new byte[0]);
        createParent("/tasks", new byte[0]);
        createParent("/status", new byte[0]);
    }


    private void createParent(String path, byte[] data) {
        zk.create(path,
                data,
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT,
                createParentCallback,
                data);
    }

    private AsyncCallback.StringCallback createParentCallback = (resultCode, path, ctx, name) -> {
        switch (KeeperException.Code.get(resultCode)) {
            case CONNECTIONLOSS:
                createParent(path, (byte[]) ctx);
                break;
            case OK:
                System.out.println(String.format("Parent %s created", path));
                break;
            case NODEEXISTS:
                System.out.println("Parent already registered:" + path);
                break;
            default:
                String errMsg = String.format("Something went wrong:%s",
                        KeeperException.create(KeeperException.Code.get(resultCode)));
                System.out.println(errMsg);
        }
    };

    public boolean isConnected() {
        return connected;
    }

    public boolean isExpired() {
        return expired;
    }

    public MasterStates getState() {
        return state;
    }

    public int wokersSize() {
        return workersCache == null ? 0 : workersCache.getList().size();
    }

    /*
     **************************************
     **************************************
     * Methods related to master election.*
     **************************************
     **************************************
     */


    /*
     * The story in this callback implementation is the following.
     * We tried to create the master lock znode. If it suceeds, then
     * great, it takes leadership. However, there are a couple of
     * exceptional situations we need to take care of.
     *
     * First, we could get a connection loss event before getting
     * an answer so we are left wondering if the operation went through.
     * To check, we try to read the /master znode. If it is there, then
     * we check if this master is the primary. If not, we run for master
     * again.
     *
     *  The second case is if we find that the node is already there.
     *  In this case, we call exists to set a watch on the znode.
     */

    private AsyncCallback.StringCallback masterCreateCallback = (resultCode, path, ctx, name) -> {
        switch (KeeperException.Code.get(resultCode)) {
            case CONNECTIONLOSS:
                checkMaster();
                break;
            case OK:
                isLeader = true;
                state = MasterStates.ELECTED;
                takeLeadership();
                break;
            case NODEEXISTS:
                isLeader = false;
                state = MasterStates.NOT_ELECTED;
                masterExists();
                break;
            default:
                isLeader = false;
                String errMsg = String.format("Something went wrong when running for master.%s",
                        KeeperException.create(KeeperException.Code.get(resultCode), path));
                System.out.println(errMsg);
        }
        System.out.println("I'm " + (isLeader ? "" : "not ") + "the leader");
    };

    void masterExists() {
        zk.exists("/master", masterExistsWatcher, masterExistsCallback, null);
    }

    private AsyncCallback.StatCallback masterExistsCallback = (rc, path, ctx, stat) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                masterExists();
            case OK:
                if (stat == null) {
                    state = MasterStates.RUNNING;
                    runForMaster();
                }
                break;
            default:
                checkMaster();
                break;
        }
    };

    private Watcher masterExistsWatcher = event -> {
        if (event.getType() == Event.EventType.NodeDeleted) {
            Assert.assertTrue("/master".equals(event.getPath()));

            runForMaster();
        }
    };


    private void takeLeadership() {
        System.out.println("Going for list of workers");
        getWorkers();

        new RecoveredAssignments(zk).recovery((rc, tasks) -> {
            if (rc == RecoveredAssignments.RecoveryCallback.FAILED) {
                System.out.println("Recovery of assigned tasks failed.");
            } else {
                System.out.println("Assigning recovered tasks");
                getTasks();
            }
        });
    }


    /*
     * Run for master. To run for master, we try to create the /master znode,
     * with masteCreateCallback being the callback implementation.
     * In the case the create call succeeds, the client becomes the master.
     * If it receives a CONNECTIONLOSS event, then it needs to check if the
     * znode has been created. In the case the znode exists, it needs to check
     * which server is the master.
     */
    void runForMaster() {
        zk.create("/master", serverId.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL, masterCreateCallback, null);
    }

    private AsyncCallback.DataCallback masterCheckCallback = (resultCode, path, ctx, data, stat) -> {
        switch (KeeperException.Code.get(resultCode)) {
            case CONNECTIONLOSS:
                checkMaster();
                return;
            case NONODE:
                runForMaster();
                return;
            case OK:

        }
    };

    private void checkMaster() {
        zk.getData("/master", true, masterCheckCallback, null);
    }


    /*
     ****************************************************
     ****************************************************
     * Methods to handle changes to the list of workers.*
     ****************************************************
     ****************************************************
     */
    private Watcher workersChangedWatcher = event -> {
        if (event.getType() == Event.EventType.NodeChildrenChanged) {
            Assert.assertTrue("/workers".equals(event.getPath()));

            getWorkers();
        }
    };

    private void getWorkers() {
        zk.getChildren("/workers", workersChangedWatcher, workersGetChildrenCallback, null);
    }

    private AsyncCallback.ChildrenCallback workersGetChildrenCallback = (rc, path, ctx, children) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                getWorkers();
                break;
            case OK:
                System.out.println("Successfully got a list of workers: "
                        + children.size()
                        + " workers");
                reassignAndSet(children);
                break;
            default:
                String errMsg = String.format("getChildren failed %s",
                        KeeperException.create(KeeperException.Code.get(rc), path));
                System.out.println(errMsg);
        }
    };


    /*
     *******************
     *******************
     * Assigning tasks.*
     *******************
     *******************
     */
    private void reassignAndSet(List<String> children) {
        List<String> toProcess;

        if (workersCache == null) {
            workersCache = new ChildrenCache(children);
            toProcess = null;
        } else {
            toProcess = workersCache.removedAndSet(children);
        }

        if (toProcess != null) {
            for (String worker : toProcess) {
                getAbsentWorkerTasks(worker);
            }
        }

    }

    /**
     * 如果有节点被删除了，就需要重新分配任务
     */
    private void getAbsentWorkerTasks(String worker) {
        System.out.println("getAbsentWorkerTasks:" + "/assign/" + worker);
        zk.getChildren("/assign/" + worker, false, workerAssignCallback, worker);
    }


    private AsyncCallback.ChildrenCallback workerAssignCallback = (rc, path, ctx, children) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                getAbsentWorkerTasks(path);
                break;
            case OK:
                System.out.println("Successfully got a list of assignments: "
                        + children.size()
                        + " tasks");
                for (String task : children) {
                    getDataReassign(path + "/" + task, task);
                }
            default:
                String errMsg = String.format("getChildren failed %s", KeeperException.create(KeeperException.Code.get(rc), path));
                System.out.println(errMsg);
        }
    };


    /*
     ************************************************
     * Recovery of tasks assigned to absent worker. *
     ************************************************
     */
    private void getDataReassign(String path, String task) {
        zk.getData(path, false, getDataReassignCallback, task);
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
                String errMsg = String.format("Something went wrong when get data.%s",
                        KeeperException.create(KeeperException.Code.get(rc), path));
                System.out.println(errMsg);
        }
    };

    private void recreateTask(RecreateTaskCtx ctx) {
        zk.create("/tasks/" + ctx.getTask(),
                ctx.getData(),
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
                deleteAssignment(((RecreateTaskCtx) ctx).getPath());
                break;
            case NODEEXISTS:
                System.out.println("Node exists already, but if it hasn't been deleted, " +
                        "then it will eventually, so we keep trying: " + path);
                recreateTask((RecreateTaskCtx) ctx);
                break;
            default:
                String errMsg = String.format("Something went wrong when recreate task.%s",
                        KeeperException.create(KeeperException.Code.get(rc), path));
                System.out.println(errMsg);
        }
    };


    private void deleteAssignment(String path) {
        zk.delete(path, -1, deleteReassignCallback, null);
    }

    private AsyncCallback.VoidCallback deleteReassignCallback = (rc, path, ctx) -> {
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

    class RecreateTaskCtx {
        private final String path;

        private final String task;

        private final byte[] data;

        public RecreateTaskCtx(String path, String task, byte[] data) {
            this.path = path;
            this.task = task;
            this.data = data;
        }

        public String getPath() {
            return path;
        }

        public String getTask() {
            return task;
        }

        public byte[] getData() {
            return data;
        }
    }


    /*
     ******************************************************
     ******************************************************
     * Methods for receiving new tasks and assigning them.*
     ******************************************************
     ******************************************************
     */
    private Watcher tasksChangeWatcher = event -> {
        if (event.getType() == Event.EventType.NodeChildrenChanged) {
            getTasks();
        }
    };


    private void getTasks() {
        zk.getChildren("/tasks", tasksChangeWatcher, tasksGetChildrenCallback, null);
    }

    private AsyncCallback.ChildrenCallback tasksGetChildrenCallback = new AsyncCallback.ChildrenCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, List<String> children) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getTasks();
                    break;
                case OK:
                    if (children != null) {
                        List<String> toProcess;
                        if (tasksCache == null) {
                            tasksCache = new ChildrenCache(children);
                            toProcess = children;
                        } else {
                            toProcess = tasksCache.addedAndSet(children);
                        }
                        if (toProcess != null) {
                            assignTasks(toProcess);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private void assignTasks(List<String> tasks) {
        if (tasks == null) {
            return;
        }
        for (String task : tasks) {
            getTaskData(task);
        }
    }

    private void getTaskData(String task) {
        zk.getData("/tasks/" + task, false, taskDataCallback, task);
    }

    private AsyncCallback.DataCallback taskDataCallback = new AsyncCallback.DataCallback() {
        @Override
        public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
            switch (KeeperException.Code.get(rc)) {
                case CONNECTIONLOSS:
                    getTaskData((String) ctx);
                    break;
                case OK:
                    List<String> list = workersCache.getList();
                    String designatedWorker = list.get(RANDOM.nextInt(list.size()));

                    String assignmentPath = "/assign/" + designatedWorker + "/" + ctx;
                    createAssignment(assignmentPath, data);
                    break;
                default:
                    String errMsg = String.format("Error when trying to get task data. %s",
                            KeeperException.create(KeeperException.Code.get(rc), path));
                    System.out.println(errMsg);
            }
        }
    };

    private void createAssignment(String assignmentPath, byte[] data) {
        zk.create(assignmentPath, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, assignTaskCallback, data);
    }

    private AsyncCallback.StringCallback assignTaskCallback = (rc, path, ctx, name) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                createAssignment(path, (byte[]) ctx);

                break;
            case OK:
                System.out.println("Task assigned correctly: " + name);
                deleteTask(name.substring(name.lastIndexOf("/") + 1));

                break;
            case NODEEXISTS:
                System.out.println("Task already assigned");

                break;
            default:
                String errMsg = String.format("Error when trying to assign task.%s",
                        KeeperException.create(KeeperException.Code.get(rc), path));
                System.out.println(errMsg);
        }
    };

    private void deleteTask(String task) {
        zk.delete("/tasks/" + task, -1, taskDeleteCallback, null);
    }

    private AsyncCallback.VoidCallback taskDeleteCallback = (rc, path, ctx) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                deleteTask(path);

                break;
            case OK:
                System.out.println("Successfully deleted " + path);

                break;
            case NONODE:
                System.out.println("Task has been deleted already");

                break;
            default:
                String errMsg = String.format("Something went wrong here, %s",
                        KeeperException.create(KeeperException.Code.get(rc), path));
                System.out.println(errMsg);
        }
    };


    public enum MasterStates {
        RUNNING, ELECTED, NOT_ELECTED
    }


    public static void main(String[] args) throws InterruptedException {
        Master m = new Master("127.0.0.1:2181");
        m.startZk();

        m.bootstrap();

        m.runForMaster();

        TimeUnit.MILLISECONDS.sleep(60000);

        m.stopZk();
    }
}
