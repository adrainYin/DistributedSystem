package zookeeperConnect;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 开源的zookeeper的客户端
 * 提供各种引用场景的实现和封装
 */
public class CuratorDemo {
    private static final String CONNECTION = "127.0.0.1:2181";


    public static void main(String[] args) throws Exception {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(CONNECTION, 10000, 2000, new RetryNTimes(5, 1000));
        curatorFramework.start();



//        try {
//            curatorFramework.getData().inBackground(new BackgroundCallback() {
//                @Override
//                public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
//                    System.out.println("可以获得的数据结点是" + new String(event.getData()));
//
//                }
//            }).forPath("/hello");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        System.in.read();


        /**
         * *********************************************************************************************
         */

        //事务处理,开启一个事务
//        curatorFramework.transaction().forOperations();


        /**
         * NodeCache
         * TreeCache
         * PathCache
         */
        final PathChildrenCache cache = new PathChildrenCache(curatorFramework, "/curator", true);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED:
                        System.out.println("结点的创建");
                        break;
                    case CHILD_UPDATED:
                        System.out.println("结点的更新");
                        break;
                    case CHILD_REMOVED:
                        System.out.println("结点的删除");
                        break;
                        default:
                            break;
                }
            }
        });

        cache.start();
        curatorFramework.create().forPath("/curator", "hello zookeeper".getBytes());
        System.in.read();

    }





}
