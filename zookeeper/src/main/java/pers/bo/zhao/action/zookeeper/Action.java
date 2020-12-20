package pers.bo.zhao.action.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

public class Action {


    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 10000, null);
        String s = zk.create("/master",
                "this is a tmp master node".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        System.out.println(s);
        Stat master = zk.exists("/master", true);
        System.out.println(master.getCtime());
        System.out.println(master.getDataLength());
        System.out.println(master);
        zk.close();
    }
}
