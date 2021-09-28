package com.reeco.transport.infrastructure.model;

import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class DataRecordSerializer implements Serializer<DataRecordMessage> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serializer.super.configure(configs, isKey);
    }

    @Override
    public byte[] serialize(String topic, DataRecordMessage data) {
        return new byte[0];
    }

    @Override
    public byte[] serialize(String topic, Headers headers, DataRecordMessage data) {
        return Serializer.super.serialize(topic, headers, data);
    }

    @Override
    public void close() {
        Serializer.super.close();
    }
}
