#!/bin/bash
set -ex

echo "Starting docker compose ..."
sudo docker-compose -f docker-compose-infras.yml up -d

sleep 1
echo "Init Cassandra Schema ..."
sudo docker exec -it cassandra bash -c "cqlsh -f /opt/initdb/schema.cql"

sleep 1
echo "Starting Reeco serivces ..."
sudo docker-compose -f docker-compose-all-in-one.yml up --build -d

echo "Done!"

exit 0