package com.xueyou.demo;

import com.sun.tools.internal.jxc.ap.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.relation.RoleUnresolved;
import java.util.StringJoiner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by wuxueyou on 2017/6/4.
 */
public class Monitor {

    private static Logger logger = LoggerFactory.getLogger(Monitor.class);

    public static void main(String[] args) {
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(monitorFlag(), 0, 3, TimeUnit.SECONDS);
    }

    public static Runnable monitorFlag() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                logger.info(String.valueOf(Constants.flag));
            }
        };
        return runnable;
    }
}
