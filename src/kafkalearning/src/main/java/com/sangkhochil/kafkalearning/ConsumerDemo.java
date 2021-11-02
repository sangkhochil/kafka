package com.sangkhochil.kafkalearning;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerDemo {
	public static void main(String[] args) {
		
		Logger logger = LoggerFactory.getLogger(ConsumerDemo.class.getName());
		Properties properties = new Properties();
		String bootStrapServer = "127.0.0.1:9092";
		String groupId = "my_first_app";
		String topic = "first_topic";

		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");		
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		
		try (KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties)) {
			consumer.subscribe(Arrays.asList(topic));
			
			while(true) {
				ConsumerRecords<String, String> consumerRecords =  consumer.poll(Duration.ofMillis(100));
				for (ConsumerRecord<String, String> record : consumerRecords) {
					logger.info("key: " + record.key() + ", value: " + record.value() + "partition: " + record.partition() + ", offset: " + record.offset() + "\n");
				}
				
			}
			
			//consumer.close();
		}
	}
}
