# Ingestion Service

## Prerequisite
- Please make sure you have already installed docker & docker-compose

## Starting project
- Just run ``` ./start_all.sh ```

## Kafka-UI
- Go to **localhost:19000** and keep track events came to the kafka topic

## Cassandra
- Table and schema have already initialized also from starting all script.
Just connect to cassandra db **localhost:9042** and keyspace **reecotech** to see all created tables.
- Sample Events from kafka broker are inserted into **numeric_series_by_organization**.


## Cassandra Schema: 

```
CREATE KEYSPACE IF NOT EXISTS reecotech
WITH REPLICATION = {
'class' : 'SimpleStrategy',
'replication_factor' : 1
};

CREATE TABLE IF NOT EXISTS reecotech.indicators(
  indicator_id BIGINT,
  group_id BIGINT,
  group_name TEXT,
  indicator_name TEXT,
  value_type TEXT,
  unit TEXT,
  standard_unit TEXT,
  PRIMARY KEY(indicator_id)
);

CREATE TABLE IF NOT EXISTS reecotech.params_by_organization(
  organization_id BIGINT,
  param_id BIGINT,
  indicator_id BIGINT,
  indicator_name TEXT,
  param_name TEXT,
  station_id BIGINT,
  connection_id BIGINT,
  PRIMARY KEY((organization_id), param_id)
) WITH CLUSTERING ORDER BY (param_id ASC);


CREATE TABLE IF NOT EXISTS reecotech.numeric_series_by_organization(
  organization_id BIGINT,
  date DATE,
  param_id BIGINT,
  indicator_name TEXT,
  param_name TEXT,
  station_id BIGINT,
  connection_id BIGINT,
  value DOUBLE,
  received_at TIMESTAMP,
  event_time TIMESTAMP,
  lat DOUBLE,
  lon DOUBLE,
  PRIMARY KEY((organization_id, date), event_time, param_id)
) WITH CLUSTERING ORDER BY (event_time DESC, param_id ASC);

CREATE TABLE IF NOT EXISTS reecotech.categorical_series_by_organization(
  organization_id BIGINT,
  date DATE,
  param_id BIGINT,
  station_id BIGINT,
  connection_id BIGINT,
  value TEXT,
  indicator_name TEXT,
  param_name TEXT,
  received_at TIMESTAMP,
  event_time TIMESTAMP,
  lat DOUBLE,
  lon DOUBLE,
  PRIMARY KEY((organization_id, date), event_time, param_id, value)
) WITH CLUSTERING ORDER BY (event_time DESC, param_id ASC, value ASC);

CREATE TABLE IF NOT EXISTS reecotech.numeric_stats_by_organization(
  organization_id BIGINT,
  param_id BIGINT,
  date DATE,
  min DOUBLE,
  max DOUBLE,
  median DOUBLE,
  mean DOUBLE,
  acc DOUBLE,
  cnt BIGINT,
  std DOUBLE,
  last_updated TIMESTAMP,
  PRIMARY KEY((organization_id, date), param_id)
) WITH CLUSTERING ORDER BY (param_id ASC);

CREATE TABLE IF NOT EXISTS reecotech.categorical_stats_by_organization(
  organization_id BIGINT,
  date DATE,
  param_id BIGINT,
  value TEXT,
  cnt BIGINT,
  last_updated TIMESTAMP,
  PRIMARY KEY((organization_id, date), param_id, value)
) WITH CLUSTERING ORDER BY (param_id ASC, value ASC);
```

## Incoming TimeSeries Event Payload
```
    Long organizationId;

    Long stationId;

    Long connectionId;

    Long paramId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime eventTime;

    Long indicatorId;

    String indicatorName;

    String paramName;

    String value;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime receivedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    LocalDateTime sentAt;

    Double lat; // nullable

    Double lon;
```

## TODO: 
- Entity Manage To Cassandra
- Scheduler for processing batching statistics
- Event Validation








 


