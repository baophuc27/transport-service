# Reeco Data Services

## Config service
- Receive config entity from UI through HTTP and push to message queue topic for further consumption.

## Ingestion service
- Receive time series events from devices through message queue and ingest data into bigtable.
- Handle batch processing daily to aggregate event metrics and statistics.
- Process & check matching alarm condition. 

## Chart Service
- Expose API endpoint to return historical data from a specific time ranges.


## Deployment
### Infrastructure
- Component:
    + Kafka - Message Broker (must)
    + Cassandra - Time series database(must)
    + Kafkadrop - Kafka UI to view event and topic(optional)
    + Zookeeper - Services Coordinator(must)
    

- Create .env file in the same root folder reco-dmp. That contain multiple env variables need to be configured:

```# cassandra config
   CASSANDRA_KEYSPACE=reecotech
   CASSANDRA_USER=yourpassword
   CASSANDRA_PASSWORD=yourpassword
   CASSANDRA_CONTACT_POINTS=localhost
   CASSANDRA_PORT=9042
   
   # kafka config
   KAFKA_BOOTSTRAP_SERVERS=localhost:29092
   KAFKA_TS_EVENT_GROUP_ID=reeco_time_series_event_cg
   KAFKA_CONFIG_EVENT_GROUP_ID=reeco_config_event_ingestion
   KAFKA_CONFIG_AUTO_OFFSET_RESET=earliest
   KAFKA_TS_EVENT_AUTO_OFFSET_RESET=earliest
   KAFKA_MAX_CONCURRENCY=1
   KAFKA_TS_EVENT_TOPIC_PARTITIONS=1
   KAFKA_TS_EVENT_TOPIC_REPLICATIONS=1
   KAFKA_CONFIG_TOPIC_PARTITIONS=1
   KAFKA_CONFIG_TOPIC_REPLICATIONS=1
   KAFKA_ALARM_TOPIC_PARTITIONS=1
   KAFKA_ALARM_TOPIC_REPLICATIONS=1
   KAFKA_ALARM_NOTI_TOPIC_PARTITIONS=1
   KAFKA_ALARM_NOTI_TOPIC_REPLICATIONS=1
   
   KAFKA_CONFIG_EVENT_HTTP_GROUP_ID=reeco_config_event_http
   
   # Resource provision
   CASSANDRA_CPUS=4
   CASSANDRA_MEMORY=4g
   
   KAFKA_CPUS=2
   KAFKA_MEMORY=2g
   
   ZK_CPUS=1
   ZK_MEMORY=1g
```

- Start infrastructure using docker-compose, please make sure that docker and docker-compose are already installed first!.
`sudo docker-compose -f docker-compose-infras.yml up -d`

### Reeco Data Services.
#### For Reeco Centralize Server Scenario.
    `sudo docker-compose -f docker-compose-reeco-services.yml --build up -d`
    

#### For Reeco Software License For other Party.
- Build in advance an image and push to your docker hub.
Run  `sudo docker-compose -f docker-compose-build-all-in-one.yml --build` and push the image you just built in your docker repo.
Then just run `sudo docker-compose -f docker-compose-run-all-in-one.yml up -d`






 


