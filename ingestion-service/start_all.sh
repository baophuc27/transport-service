#!/bin/bash
set -ex

echo "Starting docker compose ..."
docker-compose -f ./docker-compose.yml up -d


echo "Init Cassandra Schema ..."
docker exec -it cassandra bash -c "cqlsh -f /opt/initdb/schema.cql"

# COPY indicators (indicator_id, created_at, group_id, indicator_name, indicator_name_vi, standard_unit, updated_at, value_type) FROM '/opt/initdb/indicator.csv' WITH DELIMITER = ',' AND HEADER = true ;

#docker exec -it cassandra bash -c "COPY cycling.cyclist_catgory FROM 'cyclist_category.csv' WITH DELIMITER='|' AND HEADER=TRUE

echo "Send some events to kafka topics ..."
docker exec -it broker bash -c "kafka-console-producer --topic reeco_time_series_event --bootstrap-server localhost:19092 < /opt/events.json "

echo "Done!"

exit 0