package com.reeco.config.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.common.model.dto.CustomId;
import com.reeco.common.model.enumtype.ActionType;
import com.reeco.common.model.enumtype.CustomIdType;
import com.reeco.common.model.enumtype.EntityType;
import com.reeco.config.exception.ValidationException;
import com.reeco.config.service.CustomIdService;
import com.reeco.config.service.KafkaService;
import com.reeco.config.service.ReecoRequestParamValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomIdServiceImpl implements CustomIdService {
    private final KafkaService kafkaService;
    private final ObjectMapper objectMapper;
    private final String TOPIC_NAME = "reeco_config_event";

    @Override
    public DeferredResult<ResponseEntity<String>> createCustomId(CustomId customIdPayload) {
        log.info("Processing custom ID creation: {}", customIdPayload);
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();

        String customIdType = customIdPayload.getCustomIdType().name().toLowerCase();
        log.debug("Custom ID type: {}", customIdType);

        try {
            // Validate custom ID type
            if (!isValidCustomIdType(customIdPayload.getCustomIdType())) {
                String message = "Unsupported custom Id type for " + customIdPayload.getCustomIdType();
                responseWriter.setResult(new ResponseEntity<>(message, HttpStatus.BAD_REQUEST));
                log.error(message);
                return responseWriter;
            }

            // Validate other fields
            ReecoRequestParamValidator<CustomId> validator = new ReecoRequestParamValidator<>();
            responseWriter = validator.getResponseMessage(customIdPayload);

            if (responseWriter.getResult() != null) {
                return responseWriter;
            }

            // Create Kafka message
            Map<String, String> headers = new HashMap<>();
            headers.put("customIdType", customIdPayload.getCustomIdType().name());

            byte[] customIdBytes = objectMapper.writeValueAsString(customIdPayload).getBytes();
            ProducerRecord<String, byte[]> record = kafkaService.createKafkaRecord(
                    TOPIC_NAME,
                    customIdPayload.getOriginalId().toString(),
                    customIdBytes,
                    ActionType.UPSERT,
                    EntityType.CUSTOMID,
                    headers
            );

            return kafkaService.sendKafkaMessage(record);

        } catch (IllegalArgumentException | JsonProcessingException ex) {
            log.error("Error processing custom ID", ex);
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return responseWriter;
    }

    @Override
    public DeferredResult<ResponseEntity<String>> deleteCustomId(String idType, Integer originalId) {
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        String customIdType = idType.toUpperCase();
        log.info("Deleting custom ID - type: {}, id: {}", customIdType, originalId);

        try {
            // Validate custom ID type
            if (!isValidCustomIdTypeString(customIdType)) {
                String message = "Unsupported custom Id type: " + customIdType;
                responseWriter.setResult(new ResponseEntity<>(message, HttpStatus.BAD_REQUEST));
                log.error(message);
                return responseWriter;
            }

            // Create custom ID object for deletion
            CustomId customIdPayload = new CustomId();
            customIdPayload.setOriginalId(String.valueOf(originalId));
            customIdPayload.setCustomIdType(CustomIdType.valueOf(customIdType));

            // Create Kafka message
            byte[] customIdBytes = objectMapper.writeValueAsString(customIdPayload).getBytes();
            ProducerRecord<String, byte[]> record = kafkaService.createKafkaRecord(
                    TOPIC_NAME,
                    String.valueOf(originalId),
                    customIdBytes,
                    ActionType.DELETE,
                    EntityType.CUSTOMID,
                    null
            );

            return kafkaService.sendKafkaMessage(record);

        } catch (IllegalArgumentException | JsonProcessingException ex) {
            log.error("Error processing custom ID deletion", ex);
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return responseWriter;
    }

    private boolean isValidCustomIdType(CustomIdType customIdType) {
        return CustomIdType.CONNECTION.equals(customIdType)
                || CustomIdType.WORKSPACE.equals(customIdType)
                || CustomIdType.PARAMETER.equals(customIdType)
                || CustomIdType.ORGANIZATION.equals(customIdType)
                || CustomIdType.STATION.equals(customIdType);
    }

    private boolean isValidCustomIdTypeString(String customIdType) {
        return CustomIdType.CONNECTION.name().equals(customIdType)
                || CustomIdType.WORKSPACE.name().equals(customIdType)
                || CustomIdType.PARAMETER.name().equals(customIdType)
                || CustomIdType.ORGANIZATION.name().equals(customIdType)
                || CustomIdType.STATION.name().equals(customIdType);
    }
}