#!/bin/sh
echo "Starting Reeco Services ..."
java -jar reeco-config-service.jar &
sleep 10 && java -jar reeco-ingestion-service.jar &
sleep 10 && java -jar reeco-core.jar
