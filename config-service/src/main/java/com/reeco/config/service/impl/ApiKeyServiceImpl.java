package com.reeco.config.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.common.model.dto.APIKey;
import com.reeco.common.model.dto.MQTTShare;
import com.reeco.common.model.enumtype.ActionType;
import com.reeco.common.model.enumtype.EntityType;
import com.reeco.config.exception.KafkaPublishException;
import com.reeco.config.service.ApiKeyService;
import com.reeco.config.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyServiceImpl implements ApiKeyService {
    private final KafkaService kafkaService;
    private final ObjectMapper objectMapper;
    private final String TOPIC_NAME = "reeco_config_event";

    @Override
    public DeferredResult<ResponseEntity<String>> upsertAPIKey(Map<String, Object> apiKeyPayload) {
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        log.info("Processing API key creation/update: {}", apiKeyPayload);

        try {
            APIKey apiKey = objectMapper.convertValue(apiKeyPayload, APIKey.class);

            byte[] apiKeyBytes = objectMapper.writeValueAsString(apiKeyPayload).getBytes();
            ProducerRecord<String, byte[]> record = kafkaService.createKafkaRecord(
                    TOPIC_NAME,
                    "1", // Default organization ID
                    apiKeyBytes,
                    ActionType.UPSERT,
                    EntityType.APIKEY,
                    Collections.emptyMap()
            );

            return kafkaService.sendKafkaMessage(record);

        } catch (IllegalArgumentException | JsonProcessingException ex) {
            log.error("Error processing API key", ex);
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return responseWriter;
    }

    @Override
    public DeferredResult<ResponseEntity<String>> deleteApiKey(String id) {
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        log.info("Deleting API key - id: {}", id);

        try {
            APIKey apiKey = new APIKey();
            apiKey.setId(id);

            byte[] apiKeyBytes = objectMapper.writeValueAsString(apiKey).getBytes();
            ProducerRecord<String, byte[]> record = kafkaService.createKafkaRecord(
                    TOPIC_NAME,
                    "1", // Default organization ID
                    apiKeyBytes,
                    ActionType.DELETE,
                    EntityType.APIKEY,
                    Collections.emptyMap()
            );

            return kafkaService.sendKafkaMessage(record);

        } catch (JsonProcessingException ex) {
            log.error("Error serializing API key for deletion", ex);
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return responseWriter;
    }

    @Override
    public DeferredResult<ResponseEntity<String>> upsertMQTTShare(Map<String, Object> mqttPayload) {
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        log.info("Processing MQTT share creation/update: {}", mqttPayload);

        try {
            MQTTShare mqtt = objectMapper.convertValue(mqttPayload, MQTTShare.class);

            byte[] mqttBytes = objectMapper.writeValueAsString(mqtt).getBytes();
            ProducerRecord<String, byte[]> record = kafkaService.createKafkaRecord(
                    TOPIC_NAME,
                    "1", // Default organization ID
                    mqttBytes,
                    ActionType.UPSERT,
                    EntityType.MQTTSHARE,
                    Collections.emptyMap()
            );

            return kafkaService.sendKafkaMessage(record);

        } catch (IllegalArgumentException | JsonProcessingException ex) {
            log.error("Error processing MQTT share", ex);
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return responseWriter;
    }

    @Override
    public DeferredResult<ResponseEntity<String>> deleteMqttShare(String id) {
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        log.info("Deleting MQTT share - id: {}", id);

        try {
            MQTTShare mqtt = new MQTTShare();
            mqtt.setId(id);

            byte[] mqttBytes = objectMapper.writeValueAsString(mqtt).getBytes();
            ProducerRecord<String, byte[]> record = kafkaService.createKafkaRecord(
                    TOPIC_NAME,
                    "1", // Default organization ID
                    mqttBytes,
                    ActionType.DELETE,
                    EntityType.MQTTSHARE,
                    Collections.emptyMap()
            );

            return kafkaService.sendKafkaMessage(record);

        } catch (JsonProcessingException ex) {
            log.error("Error serializing MQTT share for deletion", ex);
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return responseWriter;
    }
}