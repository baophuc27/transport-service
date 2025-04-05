package com.reeco.config.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.common.model.dto.Parameter;
import com.reeco.common.model.enumtype.ActionType;
import com.reeco.common.model.enumtype.EntityType;
import com.reeco.config.exception.KafkaPublishException;
import com.reeco.config.exception.ValidationException;
import com.reeco.config.service.KafkaService;
import com.reeco.config.service.ParameterService;
import com.reeco.config.service.ReecoRequestParamValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParameterServiceImpl implements ParameterService {
    private final KafkaService kafkaService;
    private final ObjectMapper objectMapper;
    private final String TOPIC_NAME = "reeco_config_event";

    @Override
    public DeferredResult<ResponseEntity<String>> createParameter(Parameter parameter) {
        log.info("Processing parameter creation: {}", parameter);

        // Validate the parameter
        ReecoRequestParamValidator<Parameter> validator = new ReecoRequestParamValidator<>();
        DeferredResult<ResponseEntity<String>> validationResult = validator.getResponseMessage(parameter);
        if (validationResult.getResult() != null) {
            return validationResult;
        }

        // Set received timestamp
        parameter.setReceivedAt(LocalDateTime.now());

        try {
            // Convert parameter to JSON
            byte[] parameterBytes = objectMapper.writeValueAsString(parameter).getBytes();

            // Create and send Kafka message
            ProducerRecord<String, byte[]> record = kafkaService.createKafkaRecord(
                    TOPIC_NAME,
                    parameter.getOrganizationId().toString(),
                    parameterBytes,
                    ActionType.UPSERT,
                    EntityType.PARAM,
                    Collections.emptyMap()
            );

            return kafkaService.sendKafkaMessage(record);
        } catch (JsonProcessingException e) {
            log.error("Error serializing parameter", e);
            throw new KafkaPublishException("Failed to process parameter", e);
        }
    }

    @Override
    public DeferredResult<ResponseEntity<String>> deleteParameter(Long id, Long connectionId, Long orgId) {
        log.info("Processing parameter deletion - id: {}, connectionId: {}, orgId: {}", id, connectionId, orgId);

        // Create parameter object for deletion
        Parameter parameter = new Parameter();
        parameter.setId(id);
        parameter.setConnectionId(connectionId);
        parameter.setOrganizationId(orgId);
        parameter.setReceivedAt(LocalDateTime.now());

        try {
            // Convert parameter to JSON
            byte[] parameterBytes = objectMapper.writeValueAsString(parameter).getBytes();

            // Create and send Kafka message
            ProducerRecord<String, byte[]> record = kafkaService.createKafkaRecord(
                    TOPIC_NAME,
                    orgId.toString(),
                    parameterBytes,
                    ActionType.DELETE,
                    EntityType.PARAM,
                    Collections.emptyMap()
            );

            return kafkaService.sendKafkaMessage(record);
        } catch (JsonProcessingException e) {
            log.error("Error serializing parameter for deletion", e);
            throw new KafkaPublishException("Failed to process parameter deletion", e);
        }
    }
}