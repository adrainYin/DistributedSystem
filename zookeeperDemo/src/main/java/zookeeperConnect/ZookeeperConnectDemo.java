package zookeeperConnect;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

public class ZookeeperConnectDemo {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1", 1000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getType() == Event.EventType.NodeCreated){
                    System.out.println(watchedEvent.getPath()+"创建了新的节点");
                }
                if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                    System.out.println("结点的数据发生了改变" + watchedEvent.getPath());
                }
            }
        });
//        byte[] data =  zooKeeper.getData("/user", false, new Stat());
        zooKeeper.create("/tmp", "hello world".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        zooKeeper.setData("/tmp", "我的数据发生了改变".getBytes(), -1);
        System.out.println(zooKeeper.getState());
//        System.out.println(new String(data));
        Thread.sleep(2000);
    }
}
