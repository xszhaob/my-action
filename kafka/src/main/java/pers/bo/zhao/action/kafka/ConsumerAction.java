package pers.bo.zhao.action.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConsumerAction {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test");
        props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.setProperty(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "false");
        props.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        Thread t = new Thread() {

            private volatile boolean closed = false;

            @Override
            public void run() {
                try {
                    List<String> list = new ArrayList<>();
                    list.add(ProducerAction.TOPIC);
                    consumer.subscribe(list);
                    while (!closed) {
                        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000000));
                        // Handle new records
                        for (ConsumerRecord<String, String> record : records) {
                            System.out.println("consumer: topic = " + record.topic() + ", value = " + record.value());
                        }
                    }
                } catch (WakeupException e) {
                    // Ignore exception if closing
                    if (!closed) throw e;
                } finally {
                    consumer.close();
                }
            }

            // Shutdown hook which can be called from a separate thread
            public void shutdown() {
                closed = true;
                consumer.wakeup();
            }
        };

        t.start();
    }
}
