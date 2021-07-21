#!/usr/bin/env bash
cd ..
mkdir shares
cd shares

spring init \
--boot-version=2.4.1 \
--build=gradle \
--java-version=1.8 \
--packaging=jar \
--name=util \
--package-name=com.marvin.share.util \
--groupId=com.marvin.share.util \
--dependencies=webflux \
--version=1.0.0-SNAPSHOT \
util

spring init \
--boot-version=2.4.1 \
--build=gradle \
--java-version=1.8 \
--packaging=jar \
--name=api \
--package-name=com.marvin.share.api \
--groupId=com.marvin.share.api \
--dependencies=webflux \
--version=1.0.0-SNAPSHOT \
api

cd ..
