package com.xueyou.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by wuxueyou on 2017/6/4.
 */
public class Consumer {
    private static Logger logger = LoggerFactory.getLogger(Consumer.class);
    public static final String BOOTSTRAP_SERVER = "192.168.0.66:9092";
    public static final String TOPICNAME="test001";

    public static void main(String[] args) {
        System.out.println("begin kafka consumer");
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(connectionKafka(), 0, 2, TimeUnit.SECONDS);
    }

    public static Runnable connectionKafka() {

        Properties props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVER);
        props.put("group.id", "testConsumer");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        //要消费的topic名称
        consumer.subscribe(Arrays.asList(TOPICNAME));

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                logger.info("running ......");
                ConsumerRecords<String, String> records = consumer.poll(0);
                if (!records.isEmpty()) {
                    Constants.checktrue();
                } else {
                    Constants.checkfalse();
                }
                logger.info(String.valueOf(Constants.flag));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s\t\n", record.offset(), record.key(), record.value());
                }
            }
        };
        return runnable;
    }


}
