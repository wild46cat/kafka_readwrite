package com.xueyou.demo;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * Hello world!
 */
public class ProducerApp {
    private static Logger logger = LoggerFactory.getLogger(ProducerApp.class);
    public static final String BOOTSTRAP_SERVER = "192.168.0.66:9092";
    public static final String TOPICNAME = "test001";
    public static Properties props;
    public static BlockingDeque<String> blockingDeque = new LinkedBlockingDeque<>(5000);
    public static boolean isStarted = false;

    static {
        props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVER);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 0);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    }

    public static void main(String[] args) {
        logger.info("begin kafka producer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        producer.send(new ProducerRecord<String, String>(TOPICNAME, "aaabbbccc"));
        producer.close();
    }

    public static boolean sendToKafka(String data) {
        boolean res = blockingDeque.offer(data);
        if (!res) {
            blockingDeque.clear();
        }
        if (!isStarted) {
            isStarted = true;
            doSendToKafka();
        }
        return res;
    }

    public static void doSendToKafka() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                List<String> list = new LinkedList<>();
                blockingDeque.drainTo(list);
                Iterator<String> it = list.iterator();
                Producer<String, String> producer = new KafkaProducer<>(props);
                while (it.hasNext()) {
                    producer.send(new ProducerRecord<String, String>(TOPICNAME, it.next()));
                }
                producer.close();
            }
        }, 1, 2, TimeUnit.SECONDS);
    }
}
