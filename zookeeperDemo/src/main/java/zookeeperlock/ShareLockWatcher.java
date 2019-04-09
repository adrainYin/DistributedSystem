package zookeeperlock;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.concurrent.CountDownLatch;

public class ShareLockWatcher implements Watcher {

    private CountDownLatch countDownLatch;

    public ShareLockWatcher(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            System.out.println("结点删除，需要通知");
            countDownLatch.countDown();
        }
    }
}
