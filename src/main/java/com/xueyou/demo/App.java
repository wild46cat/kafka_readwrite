package com.xueyou.demo;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by wuxueyou on 2018/1/16.
 */
public class App {
    public static void main(String[] args) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                ProducerApp.sendToKafka("aabbccddeeff");
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
}
