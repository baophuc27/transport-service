package com.reeco.config.service;

import com.reeco.common.model.enumtype.ActionType;
import com.reeco.common.model.enumtype.EntityType;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;

public interface KafkaService {
    DeferredResult<ResponseEntity<String>> sendKafkaMessage(ProducerRecord<String, byte[]> message);

    ProducerRecord<String, byte[]> createKafkaRecord(String topic, String key, byte[] value,
                                                     ActionType actionType, EntityType entityType,
                                                     Map<String, String> additionalHeaders);
}