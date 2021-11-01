#!/bin/bash
set -ex

echo "Starting docker compose ..."
docker-compose -f ./docker-compose.yml up -d


echo "Init Cassandra Schema ..."
docker exec -it cassandra bash -c "cqlsh -f /opt/schema.cql"


echo "Send some events to kafka topics ..."
docker exec -it broker bash -c "kafka-console-producer --topic reeco_time_series_event --bootstrap-server localhost:19092 < /opt/events.json "

echo "Done!"

exit 0