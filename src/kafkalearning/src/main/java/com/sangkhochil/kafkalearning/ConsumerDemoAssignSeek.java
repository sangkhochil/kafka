package com.sangkhochil.kafkalearning;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerDemoAssignSeek {
	public static void main(String[] args) {
		
		Logger logger = LoggerFactory.getLogger(ConsumerDemoAssignSeek.class.getName());
		Properties properties = new Properties();
		String bootStrapServer = "127.0.0.1:9092";
		String topic = "first_topic";

		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");	
		
		try (KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties)) {
			TopicPartition partition = new TopicPartition(topic, 0);
			consumer.assign(Arrays.asList(partition));
			
			long offsetFrom = 50l;
			consumer.seek(partition, offsetFrom);
			int read = 5;
			boolean reading = true;
			int count = 1;
			
			while(reading) {
				ConsumerRecords<String, String> consumerRecords =  consumer.poll(Duration.ofMillis(100));
				for (ConsumerRecord<String, String> record : consumerRecords) {
					logger.info("key: " + record.key() + ", value: " + record.value() + "partition: " + record.partition() + ", offset: " + record.offset() + "\n");
					
					if(read == count) {
						reading = false;
						break;
					}
					count++;
				}
				
			}
			
			//consumer.close();
		}
	}
}
