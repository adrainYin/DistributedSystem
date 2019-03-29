package com.ych.dubbo.provider;

import com.ych.dubbo.api.DubboDemoApi;
import org.springframework.stereotype.Service;

public class DubboDemoApiImpl implements DubboDemoApi {

    @Override
    public String sayHello(String content) {
        System.out.println("我已经提供了sayHello接口的实现");
        return"你好" + content;
    }
}
