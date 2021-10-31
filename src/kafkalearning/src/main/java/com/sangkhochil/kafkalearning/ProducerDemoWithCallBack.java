package com.sangkhochil.kafkalearning;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerDemoWithCallBack {
	private static Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallBack.class);
	private String bootStrapServer = "127.0.0.1:9092";

	public void Producer() {

		Properties configs = new Properties();
		configs.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
		configs.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		configs.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

		for (int i = 0; i < 10; i++) {
			// create producer
			KafkaProducer<String, String> producer = new KafkaProducer<String, String>(configs);

			// send data
			ProducerRecord<String, String> record = new ProducerRecord<String, String>("first_topic",
					"hello world with callback-"+i);
			producer.send(record, new Callback() {

				@Override
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					if (exception == null) {
						logger.info("topic: " + metadata.topic() + "\n" + "partition: " + metadata.partition() + "\n"
								+ "offset: " + metadata.offset() + "\n" + "timestamp: " + metadata.timestamp());
					} else {
						logger.error("error occure", exception);
					}
				}
			});

			producer.flush();
			producer.close();
		}

	}
}
