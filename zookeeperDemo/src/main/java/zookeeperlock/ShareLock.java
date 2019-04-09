package zookeeperlock;

import com.mysql.cj.util.TimeUtil;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ShareLock {

    private ZooKeeper zooKeeper;
    private int sessionTime;
    private String currLockId;
    private static final String LOCK = "/sharelock";
    private static final int SESSION_TIME = 5000;
    private String lockID;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private static final String VALUE = null;

    public ShareLock() throws IOException, InterruptedException {
        this.zooKeeper = ZookeeperFactory.getInstance();
        this.sessionTime = SESSION_TIME;
    }

    public synchronized boolean lock() throws KeeperException, InterruptedException {
        lockID = zooKeeper.create(LOCK + "/", "123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(Thread.currentThread().getName() + "成功创建了临时有序结点，结点号:" + lockID);

        List<String> lockIdList = zooKeeper.getChildren(LOCK, true);
        Collections.sort(lockIdList);
        //该结点是首结点
        if (lockIdList.get(0).equals(lockID)) {
            System.out.println(Thread.currentThread().getName() + "成功获取锁" + "id = " + lockID);
            return true;
        }
        int currIndex = lockIdList.indexOf(lockID);
        String prevLockId = lockIdList.get(currIndex - 1);
        //这里会发生阻塞
        zooKeeper.exists(LOCK + "/" + prevLockId,new ShareLockWatcher(countDownLatch));
        //或者session的连接超时
        countDownLatch.await(sessionTime, TimeUnit.MILLISECONDS);
        return true;
    }

    public synchronized boolean unLock() throws KeeperException, InterruptedException {
        System.out.println(Thread.currentThread().getName() + "开始释放锁");
        zooKeeper.delete(LOCK + "/" + lockID, -1);
        System.out.println(Thread.currentThread().getName() + "成功释放锁");
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ShareLock lock = null;
                    try {
                        lock = new ShareLock();
//                        System.out.println(Thread.currentThread().getName() + "创建成功");
                        try {
                            latch.countDown();
                            latch.await();
                            lock.lock();
                            Thread.sleep(new Random(500).nextInt());
                        } catch (KeeperException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            lock.unLock();
                        } catch (KeeperException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        }
        latch.await();
    }

}
