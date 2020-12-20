package pers.bo.zhao.action.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class Client implements Watcher {

    private ZooKeeper zk;
    private String hostPort;
    private volatile boolean connected;
    private volatile boolean expired;
    private ConcurrentHashMap<String, Object> taskCtxMap = new ConcurrentHashMap<>();

    public Client() {
        this("127.0.0.1:2181");
    }

    public Client(String hostPort) {
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
        if (zk != null) {
            try {
                zk.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println(event.toString());
        if (event.getType() == Event.EventType.None) {
            switch (event.getState()) {
                case SyncConnected:
                    connected = true;
                    break;
                case Disconnected:
                    connected = false;
                    break;
                case Expired:
                    connected = false;
                    expired = false;
                    break;
                default:
                    break;
            }
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isExpired() {
        return expired;
    }

    void submitTask(String task, TaskObject taskCtx) {
        taskCtx.setTask(task);
        zk.create("/tasks/task-",
                task.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                // 有序节点
                CreateMode.PERSISTENT_SEQUENTIAL,
                createTaskCallback, taskCtx);
    }


    private AsyncCallback.StringCallback createTaskCallback = (resultCode, path, ctx, name) -> {
        switch (KeeperException.Code.get(resultCode)) {
            case CONNECTIONLOSS:
                TaskObject taskCtx = (TaskObject) ctx;
                submitTask(taskCtx.getTask(), taskCtx);
                break;
            case OK:
                System.out.println("My created task name: " + name);
                ((TaskObject) ctx).setTaskName(name);
                watchStatus(name.replace("/tasks/", "/status/"), ctx);
                break;
            default:
                String errMsg = String.format("Something went wrong: %s",
                        KeeperException.create(KeeperException.Code.get(resultCode), path));
                System.out.println(errMsg);
        }
    };

    private void watchStatus(String path, Object ctx) {
        taskCtxMap.put(path, ctx);
        zk.exists(path, statusWatcher, existsCallback, ctx);
    }


    private AsyncCallback.DataCallback getDataCallback = (rc, path, ctx, data, stat) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                getTaskStatus(path);
                break;
            case OK:
                String taskResult = new String(data);
                System.out.println("Task " + path + ", state: " + taskResult);

                Assert.assertTrue(ctx != null);
                ((TaskObject) ctx).setStatus(taskResult.contains("done"));
                deleteTask(path);
                break;
            case NONODE:
                System.out.println("Status node is gone!");
                break;
            default:

        }
    };

    private void deleteTask(String path) {
        zk.delete(path, -1, deleteTaskCallback, null);
    }

    private AsyncCallback.VoidCallback deleteTaskCallback = (rc, path, ctx) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                deleteTask(path);
                break;
            case OK:
                System.out.println("Successfully deleted " + path);
                break;
            case NONODE:
                break;
            default:
                System.out.println("Something went wrong here, " +
                        KeeperException.create(KeeperException.Code.get(rc), path));

        }
    };

    private void getTaskStatus(String taskStatusPath) {
        zk.getData(taskStatusPath, false, getDataCallback, taskCtxMap.get(taskStatusPath));

    }

    private Watcher statusWatcher = event -> {
        if (event.getType() == Event.EventType.NodeCreated) {
            Assert.assertTrue(event.getPath().startsWith("/status/task-"));
            Assert.assertTrue(taskCtxMap.containsKey(event.getPath()));

            getTaskStatus(event.getPath());
        }
    };

    private AsyncCallback.StatCallback existsCallback = (rc, path, ctx, stat) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                watchStatus(path, ctx);
                break;
            case OK:
                System.out.println("Status node is there: " + path);
                if (stat != null) {
                    getTaskStatus(path);
                    System.out.println("Status node is there: " + path);
                }
                break;
            case NONODE:
                break;
            default:
                System.out.println("Something went wrong when " +
                        "checking if the status node exists: " +
                        KeeperException.create(KeeperException.Code.get(rc), path));
                break;
        }
    };


    static class TaskObject {
        private String task;
        private String taskName;
        private boolean done = false;
        private boolean successful = false;

        private CountDownLatch latch = new CountDownLatch(1);

        public String getTask() {
            return task;
        }

        public void setTask(String task) {
            this.task = task;
        }

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public void setStatus(boolean status) {
            this.successful = status;
            this.done = true;
            latch.countDown();
        }

        public void waitUntilDone() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                System.out.println("InterruptedException while waiting for task to get done");
            }
        }

        public boolean isDone() {
            return done;
        }

        public void setDone(boolean done) {
            this.done = done;
        }

        public boolean isSuccessful() {
            return successful;
        }

        public void setSuccessful(boolean successful) {
            this.successful = successful;
        }
    }
}
