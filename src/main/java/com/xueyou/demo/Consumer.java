package com.xueyou.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by wuxueyou on 2017/6/4.
 */
public class Consumer {
    public static void main(String[] args) {
        System.out.println("begin consumer");
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(connectionKafka(), 0, 2, TimeUnit.SECONDS);
        System.out.println("finish consumer");
    }

    public static Runnable connectionKafka() {

        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.0.84:9092,192.168.0.85:9092,192.168.0.86:9092");
//        props.put("bootstrap.servers", "192.168.0.88:9092");
        props.put("group.id", "testConsumer");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        //要消费的topic名称
        consumer.subscribe(Arrays.asList("test"));

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s\t\n", record.offset(), record.key(), record.value());
                }
            }
        };
        return runnable;

    }
}
