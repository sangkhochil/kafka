package com.sangkhochil.kafkalearning;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerDemo {
	String bootStrapServer = "127.0.0.1:9092";
	
	public void Producer() {
		
		Properties configs = new Properties();
		configs.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
		configs.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		configs.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		
		//create producer
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(configs);
		
		//send data
		ProducerRecord<String, String> record = new ProducerRecord<String, String>("first_topic", "hello world");
		producer.send(record);
		
		producer.flush();
		producer.close();
	}
}
