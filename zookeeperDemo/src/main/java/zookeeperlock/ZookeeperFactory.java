package zookeeperlock;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import zookeeperConnect.ZookeeperConnectDemo;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperFactory {

    private static final String CONNECT = "127.0.0.1:2181";
    private static final int SESSION_TIME = 5000;
    private static final String LOCK = "/sharelock";

    public static ZooKeeper getInstance() throws IOException, InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final ZooKeeper zooKeeper = new ZooKeeper(CONNECT, SESSION_TIME, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    countDownLatch.countDown();
                    System.out.println("连接获取成功");
                }
            }
        });

        countDownLatch.await();
        return zooKeeper;
    }
}
