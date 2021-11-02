# Kafka
### Setup
Download kafka binary from apache site.
Note: kafka latest version not working properly in windows file system, older(2.12-2.8.1) version works properly.

1. set environment variable with kafka downloaded bin directory.
2. make data folder for kafka & zookeeper inside kafka root directory(it could be any directory)
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

### How to create topic

kafka-topics --zookeeper 127.0.0.1:2181 --topic first_topic --create

	Missing required argument "[partitions]"
	have to mension partitions during topic creation.

kafka-topics --zookeeper 127.0.0.1:2181 --topic first_topic --create --partitions 3

	Missing required argument "[replication-factor]"

kafka-topics --zookeeper 127.0.0.1:2181 --topic first_topic --create --partitions 3 --replication-factor 2

	WARNING: Due to limitations in metric names, topics with a period ('.') or underscore ('_') could collide. To avoid
	 issues it is best to use either, but not both.
	Error while executing topic command : Replication factor: 2 larger than available brokers: 1
	
	because of started only one broker at this time.
	
	with 1 replication-factor, created successfully

kafka-topics --zookeeper 127.0.0.1:2181 --topic first_topic --create --partitions 3 --replication-factor 1

	Created topic first_topic.

How to know topic is created

	kafka-topics --zookeeper 127.0.0.1:2181 --list
	first_topic

More about topic:

	kafka-topics --zookeeper 127.0.0.1:2181 --topic first_topic --describe

Topic: first_topic

	    TopicId  4OKP_scsTFiB5qyOmDtmRw PartitionCount: 3       ReplicationFactor: 1    Configs:
	        Topic: first_topic      Partition: 0    Leader: 0       Replicas: 0     Isr: 0
	        Topic: first_topic      Partition: 1    Leader: 0       Replicas: 0     Isr: 0
	        Topic: first_topic      Partition: 2    Leader: 0       Replicas: 0     Isr: 0
	        
	        
How to delete topic

	kafka-topics --zookeeper 127.0.0.1:2181 --topic second_topic --create --partitions 3 --replication-factor 1
	WARNING: Due to limitations in metric names, topics with a period ('.') or underscore ('_') could collide. To avoid issues it is best to use either, but not both.
	Created topic second_topic.
	
	C:\Users\SRBD>kafka-topics --zookeeper 127.0.0.1:2181 --list
	first_topic
	second_topic
	C:\Users\SRBD>kafka-topics --zookeeper 127.0.0.1:2181 --topic second_topic --delete
	Topic second_topic is marked for deletion.
	Note: This will have no impact if delete.topic.enable is not set to true.

	Note: In windows there are long term bug when delete topic from kafka,
	immediately crash the kafka server in windows.
	just disable the topic but not delete actually in windows.

### Kafka console producer CLI

kafka-console-producer --broker-list 127.0.0.1:9092 --topic first_topic

	>jahangir
	>alam
	>hello
	>it' awesome course
	>bye
	>^CTerminate batch job (Y/N)? y

kafka-console-producer --broker-list 127.0.0.1:9092 --topic first_topic --producer-property acks=all

	>some mgs for acks
	>just for fun
	>fun learning...
	>^CTerminate batch job (Y/N)? y

kafka-console-producer --broker-list 127.0.0.1:9092 --topic new_topic --producer-property acks=all

	>this new topic but exist
	[2021-10-31 10:45:45,768] WARN [Producer clientId=console-producer] Error while fetching metadata with correlation id 3 : {new_topic=LEADER_NOT_AVAILABLE} (org.apache.kafka.clients.NetworkClient)
	>this new topic but now exist
	>^CTerminate batch job (Y/N)? y
	
	If kafka producer does not find any topic then showing warning and after that create topic with default partition number.
	partitions number is defined on server.properties file
	
	# The default number of log partitions per topic. More partitions allow greater
	# parallelism for consumption, but this will also result in more files across
	# the brokers.
	num.partitions=3

kafka-topics --zookeeper 127.0.0.1:2181 --list

	first_topic
	new_topic

kafka-topics --zookeeper 127.0.0.1:2181 --topic new_topic --describe

	Topic: new_topic        TopicId: BrTVfSNVRM6hBi0nxOyzcg PartitionCount: 1       ReplicationFactor: 1    Configs:
	        Topic: new_topic        Partition: 0    Leader: 0       Replicas: 0     Isr: 0



### Bidirection Compability
Means:
Older kafka client can call to newer broker
Newer kafka client can call to older broker