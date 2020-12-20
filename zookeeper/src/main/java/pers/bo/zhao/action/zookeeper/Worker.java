package pers.bo.zhao.action.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Worker implements Watcher {

    private ZooKeeper zk;
    private String hostPort;
    private String serverId = Integer.toHexString(new Random().nextInt());
    private volatile boolean connected = false;
    private volatile boolean expired = false;

    private String status;
    private String name;
    private ThreadPoolExecutor executor;
    private ChildrenCache assignedTasksCache = new ChildrenCache();


    public Worker() {
        this("127.0.0.1:2181");
    }

    public Worker(String hostPort) {
        this.hostPort = hostPort;
        this.executor = new ThreadPoolExecutor(1, 1,
                1000L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(200));
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
        System.out.println(String.format("%s,%s", event.toString(), hostPort));
        // 使用None表示无事件发生，而是Zookeeper的会话状态发生了变化。
        if (event.getType() == Event.EventType.None) {
            switch (event.getState()) {
                case SyncConnected:
                    // 注册成功
                    connected = true;
                    break;
                case Disconnected:
                    // 连接断开
                    connected = false;
                    break;
                case Expired:
                    // 会话过期，断开连接
                    connected = false;
                    expired = true;
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


    public void bootstrap() {
        createAssignNode();
    }

    private void createAssignNode() {
        zk.create("/assign/worker-" + serverId,
                new byte[0],
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT,
                createAssignCallback, null);
    }


    private AsyncCallback.StringCallback createAssignCallback = (rc, path, ctx, name) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                createAssignNode();
            case OK:
                System.out.println("Assign node created");
                break;
            case NODEEXISTS:
                System.out.println("Assign node already registered");
                break;
            default:
                System.out.println("Something went wrong: "
                        + KeeperException.create(KeeperException.Code.get(rc), path));
                break;
        }
    };


    public void register() {
        name = "worker-" + serverId;
        zk.create("/workers/" + name,
                "Idle".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                // 临时节点
                CreateMode.EPHEMERAL,
                createWorkerCallback,
                null);
    }

    private AsyncCallback.StringCallback createWorkerCallback = (resultCode, path, ctx, name) -> {
        switch (KeeperException.Code.get(resultCode)) {
            case CONNECTIONLOSS:
                register();
                break;
            case OK:
                System.out.println("Registered successfully: " + serverId);
                break;
            case NODEEXISTS:
                System.out.println("Already registered: " + serverId);
                break;
            default:
                String errMsg = String.format("Something went wrong: %s",
                        KeeperException.create(KeeperException.Code.get(resultCode), path));
                System.out.println(errMsg);
        }
    };


    public void setStatus(String status) {
        this.status = status;
        updateStatus(status);
    }

    private synchronized void updateStatus(String status) {
        // 在更新前检查当前状态和要更新的状态是否一致，否则可能会导致更新的请求乱序
        if (status.equals(this.status)) {
            zk.setData("/workers/" + name,
                    status.getBytes(),
                    // -1表示禁止版本号检查
                    -1,
                    statusUpdateCallback, status);
        }
    }

    private AsyncCallback.StatCallback statusUpdateCallback = (resultCode, path, ctx, stat) -> {
        switch (KeeperException.Code.get(resultCode)) {
            case CONNECTIONLOSS:
                updateStatus((String) ctx);
                return;
            default:
                break;
        }
    };

    private int executionCount;

    // TODO: 2019/4/14 等待解答
    synchronized void changeExecutionCount(int countChange) {
        executionCount += countChange;
        if (executionCount == 0 && countChange < 0) {
            // we have just become idle
            setStatus("Idle");
        }
        if (executionCount == 1 && countChange > 0) {
            // we have just become idle
            setStatus("Working");
        }
    }


    /*
     ***************************************
     ***************************************
     * Methods to wait for new assignments.*
     ***************************************
     ***************************************
     */
    private Watcher newTaskWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == Event.EventType.NodeChildrenChanged) {
                Assert.assertTrue(("/assign/" + name).equals(event.getPath()));

                getTasks();
            }
        }
    };


    void getTasks() {
        zk.getChildren("/assign/" + name, newTaskWatcher, tasksGetChildrenCallback, null);
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
                        executor.execute(new Runnable() {
                            List<String> children;

                            /*
                             * Initializes input of anonymous class
                             */
                            Runnable init(List<String> children) {
                                this.children = children;

                                return this;
                            }

                            @Override
                            public void run() {
                                if (children == null) {
                                    return;
                                }

                                System.out.println("Looping into tasks" + children);
                                setStatus("Working");
                                for (String task : children) {
                                    getTaskData(task);
                                }
                            }
                        }.init(assignedTasksCache.addedAndSet(children)));
                    }
                default:
                    System.out.println("getChildren failed: " + KeeperException.create(KeeperException.Code.get(rc), path));
            }
        }
    };

    private void getTaskData(String task) {
        zk.getData("/assign/worker-" + serverId + "/" + task,
                false,
                taskDataCallback,
                task);
    }

    private AsyncCallback.DataCallback taskDataCallback = (rc, path, ctx, data, stat) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                getTaskData((String) ctx);
                break;
            case OK:
                executor.execute(new Runnable() {
                    byte[] data;
                    Object ctx;

                    /*
                     * Initializes the variables this anonymous class needs
                     */
                    Runnable init(byte[] data, Object ctx) {
                        this.data = data;
                        this.ctx = ctx;

                        return this;
                    }

                    @Override
                    public void run() {
                        System.out.println("Executing your task: " + new String(data));
                        taskDone((String) ctx);
                        zk.delete("/assign/worker-" + serverId + "/" + ctx,
                                -1, taskVoidCallback, null);
                    }
                }.init(data, ctx));
                break;
            default:
                System.out.println("Failed to get task data: " + KeeperException.create(KeeperException.Code.get(rc), path));
                break;
        }
    };

    private void taskDone(String task) {
        zk.create("/status/" + task, "done".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT, taskStatusCreateCallback, task);
    }

    private AsyncCallback.StringCallback taskStatusCreateCallback = (rc, path, ctx, name) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                taskDone((String) ctx);
                break;
            case OK:
                System.out.println("Created status znode correctly: " + name);
                break;
            case NODEEXISTS:
                System.out.println("Node exists: " + path);
                break;
            default:
                System.out.println("Failed to create task data: " +
                        KeeperException.create(KeeperException.Code.get(rc), path));
        }
    };

    private AsyncCallback.VoidCallback taskVoidCallback = (rc, path, rtx) -> {
        switch (KeeperException.Code.get(rc)) {
            case CONNECTIONLOSS:
                break;
            case OK:
                System.out.println("Task correctly deleted: " + path);
                break;
            default:
                System.out.println("Failed to delete task data" +
                        KeeperException.create(KeeperException.Code.get(rc), path));
        }
    };

    public static void main(String[] args) throws IOException, InterruptedException {
        Worker worker = new Worker();
        worker.startZk();

        worker.register();

        TimeUnit.MILLISECONDS.sleep(30000);
    }
}
