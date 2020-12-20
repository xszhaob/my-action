package pers.bo.zhao.action.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Date;

public class AdminClient implements Watcher {
    private ZooKeeper zk;
    private String hostPort;

    public AdminClient() {
        this("127.0.0.1:2181");
    }

    public AdminClient(String hostPort) {
        this.hostPort = hostPort;
    }

    public void startZk() throws IOException {
        this.zk = new ZooKeeper(hostPort, 15000, this);
    }


    private void listState() throws KeeperException, InterruptedException {
        try {
            Stat stat = new Stat();
            byte[] data = zk.getData("/master", false, stat);
            Date startDate = new Date(stat.getCtime());
            System.out.println("Master: " + new String(data) + " since " + startDate);
        } catch (KeeperException.NoNodeException e) {
            System.out.println("No master");
        }

        System.out.println("Workers:");

        for (String child : zk.getChildren("/workers", false)) {
            byte[] data = zk.getData("/workers/" + child, false, null);
            String state = new String(data);
            System.out.println("\t" + child + ": " + state);
        }

        System.out.println("Tasks:");
        for (String child : zk.getChildren("/assign", false)) {
            System.out.println("\t" + child);
        }
    }


    @Override
    public void process(WatchedEvent event) {
        System.out.println(event.toString());
    }


    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        AdminClient adminClient = new AdminClient();
        adminClient.startZk();

        adminClient.listState();
    }
}
