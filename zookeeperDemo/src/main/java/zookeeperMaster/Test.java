package zookeeperMaster;


import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.concurrent.CountDownLatch;

public class Test {

    private static final String CONNECTION_STRING = "127.0.0.1:2181";

    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(10);
//        for (int i = 0; i < 10; i++) {
//            MasterSelect select = new MasterSelect(new ZkClient(CONNECTION_STRING, 5000, 5000, new SerializableSerializer()), new UserConfig(i, "客户端" + i));
//            new Thread(new MasterThread(select)).start();
//        }
        MasterSelect select = new MasterSelect(new ZkClient(CONNECTION_STRING, 5000, 5000, new SerializableSerializer()), new UserConfig(1,"客户端1"));
        select.start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
