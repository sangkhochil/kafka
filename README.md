# Kafka
### Setup
Download kafka binary from apache site.
Note: kafka latest version not working properly in windows file system, older works properly.

1. set environment variable with kafka downloaded bin sdirectory.
2. make data folder for kafka & zookeeper inside kafka root directoty(it could be any directory)
	kafka_root_dir/data/kafka
	kafka_root_dir/data/zookeeper
3. change server.properties & zookeeper.properties file with data directory.
    
	server.properties
	log.dirs=D:/kafka_2.12-2.8.1/data/kafka
	
	zookeeper.properties
	dataDir=D:/kafka_2.12-2.8.1/data/zookeeper
4. run zookeeper
   navigate kafka root directory and run
   zookeper-server-start config/zookeeper.properties
   
   zookeeper run on local host 127.0.0.1:2181
   log looks like:
   [2021-10-30 16:18:21,302] INFO binding to port 0.0.0.0/0.0.0.0:2181 (org.apache.zookeeper.server.NIOServerCnxnFactory)
5. run kafka
   navigate kafka root directory and run
   kafka-server-start config/kafka.properties
   
   log looks like:
   [2021-10-30 16:20:01,599] INFO [KafkaServer id=0] started (kafka.server.KafkaServer)
   per above command create one broker.
