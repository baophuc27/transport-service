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

and runing cql to init Database.
`sudo docker exec -it cassandra bash -c "cqlsh -u ${CASSANDRA_USER} -p ${CASSANDRA_PASSWORD} -f /opt/initdb/schema.cql`

### Reeco Data Services.
#### For Reeco Centralize Server Scenario.
    `sudo docker-compose -f docker-compose-reeco-services.yml --build up -d`
    

#### For Reeco Software License.
- Build in advance an image and push to your docker hub.
Run  `sudo docker-compose -f docker-compose-build-all-in-one.yml --build` and push the image you just built in your docker repo.
Then just run `sudo docker-compose -f docker-compose-run-all-in-one.yml up -d`

------------------------------------------------------------------

## Kafka Info
### Time series event
- Topic name: `reeco_time_series_event`
- Bootstrap server: `{host}:29092`
- UI view kafka messages: `http://{host}:19000/topic/reeco_time_series_event`
- Host: localhost
- Payload: 
```json
{
   "organizationId":"Long",
   "workspaceId":"Long",
   "stationId":"Long",
   "connectionId":"Long",
   "paramId":"Long",
   "eventTime":"yyyy-mm-dd hh-MM-ss.sss",
   "indicatorId":"Long",
   "indicatorName":"String",
   "paramName":"String",
   "value":"String",
   "receivedAt":"yyyy-mm-dd hh-MM-ss.sss",
   "sentAt":"yyyy-mm-dd hh-MM-ss.sss",
   "lat":"Double",
   "lon":"Double"
}
```
### Alarm dashboard
- Topic name: `reeco_alarm_event`
- Bootstrap server: `{host}:29092`
- UI view kafka messages: `http://{host}:19000/topic/reeco_alarm_event`
- Host: localhost
- Payload:
```json
{
  "organizationId":"Long",
  "paramId":"Long",
  "alarmId":"Long",
  "value":"String",
  "eventTime":"yyyy-mm-dd HH:MM:SS",
  "sentAt":"yyyy-mm-dd HH:MM:SS"
}
```

### Notification data
- Topic name: `reeco_alarm_noti_event`
- Bootstrap server: `{host}:29092`
- UI view kafka messages: `http://{host}:19000/topic/reeco_alarm_noti_event`
- Host: localhost
- Payload:
```json
{
  "organizationId":"Long",
  "paramId":"Long",
  "alarmId":"Long",
  "value":"String",
  "eventTime":"yyyy-mm-dd HH:MM:SS",
  "sentAt":"yyyy-mm-dd HH:MM:SS"
}
```

### Notification connection
- Topic name: `reeco_connection_noti_event`
- Bootstrap server: `{host}:29092`
- Url UI view kafka messages: `http://{host}:19000/topic/reeco_connection_noti_event`
- Host: localhost
- Payload:
```json
{
  "organizationId":"Long",
  "connectionId":"Long",
  "message":"[CONNECTED, DISCONNECTED]",
  "lastEventTime":"yyyy-mm-dd HH:MM:SS",
  "sentAt":"yyyy-mm-dd HH:MM:SS"
}
```

------------------------------------------------------------------------------

## API Document

### API upsert connection FTP
#### Request
- Method: POST
- URL: `http://{host}:{port}/config/connection/ftp`
- Port: 8986
- Host: localhost
- Param: null
- Content-Type: `application/json`
- Body: Example
```json
  {
  "id": 4,
  "organizationId": 4,
  "stationId": 4,
  "workspaceId": 1,
  "userName": "reecotech", (unique)
  "hostName": "103.88.122.104",
  "port": 2100,
  "password": "12345678",
  "useSSL": true,
  "active": true/false,
  "key": "string",
  "ipWhiteList": [
    "115.111.111.222",
    "115.235.111.333"
  ],
  "fileType": "TXT",
  "templateFormat": 2,
  "maximumTimeout": 30,
  "maximumAttachment": 100,
  "notificationType": "NEVER",
  "removeAfterDays": 30,
  "englishName": "hello",
  "vietnameseName": "hola",
  "protocol": "FTP"
}

```

#### Response
``` 
Status: 200, Message: Send Success!
Status: 400, Params x can not be empty!
Status: 500, Internal Server Error!

```


### API delete connection FTP
#### Request
- Method: DELETE
- URL: `http://{host}:{port}/config/connection/ftp/{organizationId}/{id}`
- Port: 8986
- Host: localhost
- organizationId: Long 
- id: Long (Connection Id) 
- Param: null
- Content-Type: null
- Body: null

#### Response
``` 
Response:
Status: 200, Message: Send Success!
Status: 500, Internal Server Error!

```

### API upsert Param
#### Request
- Method: POST
- URL: `http://{host}:{port}/config/parameter`
- Port: 8986
- Host: localhost
- Param: null
- Content-Type: `application/json`
- Body: Example
```json
  {
  "id": 1,
  "connectionId": 1,
  "organizationId": 1,
  "stationId": 1,
  "workspaceId": 1,
  "englishName": "String",
  "vietnameseName": "String",
  "parameterType": "CONFIGURE",
  "indicatorId": "Long", //(important)
  "keyName" : "STRING", //(important)
  "displayType": "String",
  "unit": "String",
  "format": "String",
  "alarms":[
    {
      "id": "Long",
      "englishName": "String",
      "vietnameseName":"String",
      "alarmType": "String",//{THRESHOLD,SQUARE_RANGE,BRACKET_RANGE}
      "minValue": "String", // if type THRESHOLD only use minvalue
      "maxValue": "String",
      "numOfMatch": "Int",
      "maintainType": "String", //{"NONE", "FIRST_TIME","MAINTAIN"}	
      "frequence": "Long",
      "frequenceType": "String" //{"S","M","H","D"}
    }
  ]
}

```
#### Response
``` 
Status: 200, Message: Send Success!
Status: 400, Params x can not be empty!
Status: 500, Internal Server Error!

```


### API delete param
#### Request
- Method: DELETE
- URL: `http://{host}:{port}/config/parameter/{organizationId}/{connectionId}/{id}`
- Port: 8986
- Host: localhost
- organizationId: Long
- connectionId: Long
- id: Long (Param Id)
- Param: null
- Content-Type: null
- Body: null

#### Response
``` 
Response:
Status: 200, Message: Send Success!
Status: 500, Internal Server Error!

```


### API get Data for Chart and Dashboard
#### Request
- Method: POST
- URL: `http://{host}:{port}/api/v1/chart/history-data`
- Port: 7001
- Host: localhost
- Param: null
- Content-Type: `application/json`
- Body: 
```json
  {
  "startTime":"yyyy-mm-ddTHH:MM:SS.SS", 
  "endTime":"yyyy-mm-ddTHH:MM:SS.SS",
  "resolution":"String",
  "parameterDtos":[
    {
      "orgId":"Long",
      "parameterId":"Long"
    }
  ]
}
```
- Note:
1. If (startTime = endTime) => 2 latest dataPoint.
2. Resolution follow format: ` “DEFAULT”, “MIN_30”, “HOUR_1”, “HOUR_2”, “HOUR_4”, “HOUR_8”, “DAY_1”, “DAY_3”, “WEEK_1”, “WEEK_2”, “MONTH_1”`

#### Response
``` json
 {
   "startTime":"DateTime",
   "endTime":"DateTime",
   "parameterDatas":[
      {
         "parameterDto":{
            "organizationId":"Long",
            "stationId":"Long",
            "parameterId":"Long",
            "parameterName":"String",
            "indicatorName":"String",
            "indicatorType":"String",
            "unit":"String"
         },
         "dataPointDtos":[
            {
               "value":"String",
               "eventTime":"DateTime",
               "isAlarm":"Boolean",
               "alarmType":"String",
               "lat":"Double",
               "lon":"Double"
            }
         ]
      }
   ]
}
```

### API Import data CSV
#### Request
- Method: POST
- URL: `http://{host}:{port}/api/v1/data/import-csv`
- Port: 7001
- Host: localhost
- Param: null
- Content-Type: `multipart/form-data`
- Body:
```
  organizationId: Long
  csvFile: file
  stationId: Long
  paramsInfo: [{“prameterId”: Long, “columnKey”: String}]

```

### API Export data CSV
#### Request
- Method: GET
- URL: `http://{host}:{port}/api/v1/data/export-csv`
- Port: 7001
- Host: localhost
- Param: null
- Content-Type: `application/json`
- Body:
```json
 {
  "startTime":"yyyy-mm-ddTHH:MM:SS.SS",
  "endTime":"yyyy-mm-ddTHH:MM:SS",
  "parameterDtos":[
    {
      "organizationId":"Long",
      "stationId":"Long",
      "parameterId":"Long"
    }
  ]
}
```
#### Response
```json
{
   "content": "StringBase64" 
}

```

### API get Data alarm
#### Request
- Method: POST
- URL: `http://{host}:{port}/api/v1/data/dataAlarm`
- Port: 7001
- Host: localhost
- Param: null
- Content-Type: `application/json`
- Body:
```json
  {
  "startTime":"yyyy-mm-ddTHH:MM:SS.SS", 
  "endTime":"yyyy-mm-ddTHH:MM:SS.SS",
  "resolution":"String",
  "parameterDtos":[
    {
      "orgId":"Long",
      "parameterId":"Long"
    }
  ]
}
```
- Note:
1. Resolution follow format: ` “DEFAULT”, “MIN_30”, “HOUR_1”, “HOUR_2”, “HOUR_4”, “HOUR_8”, “DAY_1”, “DAY_3”, “WEEK_1”, “WEEK_2”, “MONTH_1”`

#### Response Example
``` json
 {
   "status": "OK",
   "message": "successful!",
   "data": {
       "startTime": "2021-11-05T07:25:00",
       "endTime": "2021-11-09T09:55:00",
       "parameterDatas": [
           {
               "parameterDto": {
                   "organizationId": 1,
                   "stationId": 1,
                   "parameterId": 16,
                   "parameterName": "Salinity",
                   "indicatorName": "Salinity",
                   "indicatorType": "NUMBER"
               },
               "dataPointDtos": [
                   {
                       "value": "0.06",
                       "eventTime": "2021-11-08T09:30:00",
                       "isAlarm": true,
                       "alarmType": "THRESHOLD",
                       "alarmId": 5
                   }
               ]
           }
       ],
       "parameterDtos": []
   },
   "timestamp": 1638770199641,
   "statusCode": 200
}
```

### API get latest time data of connection
#### Request
- Method: GET
- URL: `http://{host}:{port}/api/v1/data/connection/latest-data`
- Port: 7001
- Host: localhost
- Param: 
```
- orgId: Long
- connectionIds: List <Long> 
```
- Content-Type: `application/json`
- Body: null

#### Response Example
``` json
  {
   "status": "OK",
   "message": "Successful!",
   "data": {
       "organizationId": 1,
       "connectionList": [
           {
               "connectionId": 2,
               "latestTime": "2021-11-25T13:29:14.99"
           },
           {
               "connectionId": 3,
               "latestTime": "2021-11-23T17:11:56.516"
           },
           {
               "connectionId": 4,
               "latestTime": "2021-11-23T17:31:56.516"
           },
           {
               "connectionId": 5,IX.
               "latestTime": "2021-11-23T17:34:56.516"
           }
       ]
   },
   "timestamp": 1638770994470,
   "statusCode": 200
}
```

### API generate template for sharing data
#### Request
- Method: POST
- URL: `http://{host}:{port}/data-sharing/request`
- Port: 7001
- Host: localhost
- Param:null
- Content-Type: `application/json`
- Body: 
```json
{
   "stationId":"Long",
   "userId":"Long",
   "requestId":"Long",
   "requestTitle":"String",
   "indicatorList":[
      {
         "id":"Long",
         "key":"string"
      }
   ],
   "lat":"double",
   "lon":"double"
}
```

#### Response
``` json
  {
    “csv-file”: "StringBase64"
 }
```

### API put file sharing data
#### Request
- Method: POST
- URL: `http://{host}:{port}/data-sharing/data`
- Port: 7001
- Host: localhost
- Param:null
- Content-Type: `multipart/form-data;`
- Body:
```
- stationId: Long, 
- userId: Long, 
- requestId: Long,
- csvFile: form-data
```


## NOTE: 
All APIs are not authenticated, So to ensure security, it is recommended to call API only in internal environment (Localhost)

