#!/bin/bash
set -ex
CASSANDRA_USER=$1
CASSANDRA_PASSWORD=$2
echo "Starting docker compose ..."
sudo docker-compose -f docker-compose-infras.yml up -d

sleep 1
echo "Init Cassandra Schema ..."
sudo docker exec -it cassandra bash -c "cqlsh -u ${CASSANDRA_USER} -p ${CASSANDRA_PASSWORD} -f /opt/initdb/schema.cql"

sleep 1
echo "Starting Reeco services ..."
sudo docker-compose -f docker-compose-run-all-in-one.yml up -d

echo "Done!"

exit 0
