package zookeeperMaster;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 实现master选举的过程
 */
public class MasterSelect {


    private ZkClient zkClient;
    private UserConfig userConfig;
    private UserConfig masterConfig; // 定义每次获取的master结点
    private static final String PATH = "/master";

    private ScheduledExecutorService service = Executors.newScheduledThreadPool(1);


    //定义是否停止的变量，类似线程池的停止参数
    private static volatile boolean isRunning = false;

    public MasterSelect(ZkClient zkClient, UserConfig userConfig) {
        this.zkClient = zkClient;
        this.userConfig = userConfig;
    }

    private IZkDataListener listener = new IZkDataListener() {
        @Override
        public void handleDataChange(String dataPath, Object data) throws Exception {

        }

        @Override
        public void handleDataDeleted(String dataPath) throws Exception {
            System.out.println("监听到路径+ " + dataPath +"释放结点，下面开始重新获取");
            chooseMaster();
        }
    };

    /**
     * 开始选主的操作
     */
    public void chooseMaster() {
        if (!isRunning) {
            System.out.println("选主服务还未开启");
        }
        try {
            zkClient.createEphemeral(PATH, userConfig);
            //将master注册为自己
            masterConfig = userConfig;

            System.out.println(userConfig.getName() + "  " + userConfig.getUserId() + "成功获取到了master结点");

            //这里使用的是lambda表达式
            service.schedule(()-> {
                releaseMaster();
            }, new Random().nextInt(100), TimeUnit.MILLISECONDS);
        } catch (ZkNodeExistsException e) {
            //如果还没有master会返回null值
            UserConfig config = zkClient.readData(PATH, true);
            if (config == null) {
                chooseMaster();
            }
            else {
                masterConfig = config;
            }
        }
    }

    /**
     * 释放master
     */
    private void releaseMaster() {
        if (checkCurrentMaster()) {
            zkClient.deleteRecursive(PATH);
            System.out.println(userConfig.getUserId() + "释放结点成功");
        }
    }

    /**
     * 判断当前的master是否是自己
     * @return
     */
    private boolean checkCurrentMaster() {
        UserConfig config = zkClient.readData(PATH, true);
        if (config.getUserId() == userConfig.getUserId() && config.getName().equals(userConfig.getName())) {
            return true;
        }
        return false;
    }

    /**
     * 结束选主的操作
     */
    public void stop() {
        if (isRunning) {
            isRunning = false;
            service.shutdown();
            zkClient.unsubscribeDataChanges(PATH, listener);
            releaseMaster();
        }
    }

    /**
     * 开始运行
     */
    public void start() {
        if (!isRunning) {
            isRunning = true;
            System.out.println("选主的服务开启" + "当前线程" + Thread.currentThread().getName() + userConfig.getUserId());
            zkClient.subscribeDataChanges(PATH, listener);
            chooseMaster();
        }
    }
}
