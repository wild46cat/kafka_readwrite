package com.xueyou.demo;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("begin kafka producer");
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(conntectionKafkaProducer(), 0, 2, TimeUnit.SECONDS);
    }

    public static Runnable conntectionKafkaProducer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.0.84:9092,192.168.0.85:9092,192.168.0.86:9092");
//        props.put("bootstrap.servers", "192.168.0.88:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("request.timeout.ms", 3000);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Producer<String, String> producer = new KafkaProducer<>(props);
                producer.send(new ProducerRecord<String, String>("test", "^_^ ok~~"));
                logger.info("producer send ok !!!");
                producer.flush();
                producer.close();
            }
        };
        return runnable;
    }

}
