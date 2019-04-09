package zookeeperConnect;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

public class ZkCliConnect {

    public static void main(String[] args) {
        ZkClient client = new ZkClient("127.0.0.1:2181", 4000);
        //每次客户端需要和服务器主动关闭连接，否则服务器启动一个连接超时的设置
        if (client != null) {
            System.out.println(client + "创建连接成功");
        }
        //这里是设置了连接的临时结点main方法结束，但是客户端还没有关闭。
        client.createEphemeral("/temp", "hello zookeeper");

        //注册订阅的消息通知
        //需要new一个监听器，并且自己实现监听的回调方法
        client.subscribeChildChanges("/temp", new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {

            }
        });



        client.close();
        System.out.println(client + "连接成功！");

    }
}
