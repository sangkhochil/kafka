package com.sangkhochil.kafkalearning;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerDemoThreads {
	public static void main(String[] args) {

		Logger logger = LoggerFactory.getLogger(ConsumerDemoThreads.class.getName());

		String bootStrapServer = "127.0.0.1:9092";
		String groupId = "my_first_app";
		String topic = "first_topic";
		CountDownLatch latch = new CountDownLatch(1);
		
		Runnable consumerRunnable = new ConsumerThread(bootStrapServer, groupId, topic, latch);
		Thread thread = new Thread(consumerRunnable);
		thread.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			logger.info("Caught shutdown hook");
			((ConsumerThread) consumerRunnable).shutDown();
		}));
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.error("Application got interrupted");
		} finally {
			logger.info("Application is closing..");
		}

	}
}

class ConsumerThread implements Runnable {

	private Logger logger = LoggerFactory.getLogger(ConsumerThread.class.getName());
	private Properties properties = new Properties();
	private CountDownLatch latch;
	KafkaConsumer<String, String> consumer;

	ConsumerThread(String bootStrapServer, String groupId, String topic, CountDownLatch latch) {
		this.latch = latch;

		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);

		consumer = new KafkaConsumer<String, String>(properties);
		consumer.subscribe(Arrays.asList(topic));
	}

	@Override
	public void run() {
		try {
			while (true) {
				ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));
				for (ConsumerRecord<String, String> record : consumerRecords) {
					logger.info("key: " + record.key() + ", value: " + record.value() + "partition: "
							+ record.partition() + ", offset: " + record.offset() + "\n");
				}
			}
		} catch (WakeupException e) {
			logger.info("Received shutdown signal..");
		} finally {
			consumer.close();
			latch.countDown();
		}
	}

	public void shutDown() {
		// is special method to interrupt comsumer.poll()
		// it throw WakeUpException
		consumer.wakeup();
	}

}
