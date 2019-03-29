package com.ych.dubbo.consumer;

import com.ych.dubbo.api.DubboDemoApi;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class DubboDemoApiConsumer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        context.start();
        System.out.println("已经启动");
        DubboDemoApi dubboDemoApi = (DubboDemoApi)context.getBean("dubboDemoApi");
        String consumerContent = dubboDemoApi.sayHello("上海");
        System.out.println(consumerContent);



        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
