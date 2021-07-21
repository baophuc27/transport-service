#!/usr/bin/env bash

cd ..
spring init \
--boot-version=2.4.1 \
--build=gradle \
--java-version=1.8 \
--packaging=jar \
--name=common \
--package-name=com.marvin.core.dmp.common \
--groupId=com.marvin.core.dmp.common \
--version=1.0.0-SNAPSHOT \
common






