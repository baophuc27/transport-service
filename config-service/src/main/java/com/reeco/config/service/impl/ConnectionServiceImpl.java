package com.reeco.config.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.common.model.dto.FTPConnection;
import com.reeco.common.model.dto.HTTPConnection;
import com.reeco.common.model.enumtype.ActionType;
import com.reeco.common.model.enumtype.EntityType;
import com.reeco.common.model.enumtype.Protocol;
import com.reeco.common.utils.AES;
import com.reeco.config.exception.KafkaPublishException;
import com.reeco.config.exception.ValidationException;
import com.reeco.config.service.ConnectionService;
import com.reeco.config.service.KafkaService;
import com.reeco.config.service.ReecoRequestParamValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionServiceImpl implements ConnectionService {
    private final KafkaService kafkaService;
    private final ObjectMapper objectMapper;
    private final String TOPIC_NAME = "reeco_config_event";

    @Override
    public DeferredResult<ResponseEntity<String>> createConnection(String protocol, Map<String, Object> connectionPayload) {
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        log.info("Processing connection creation with protocol {}: {}", protocol, connectionPayload);

        try {
            if (!Protocol.FTP.name().toLowerCase().equals(protocol)
                    && !Protocol.FTPS.name().toLowerCase().equals(protocol)
                    && !Protocol.HTTP.name().toLowerCase().equals(protocol)) {
                String message = "Unsupported connection method " + protocol;
                responseWriter.setResult(new ResponseEntity<>(message, HttpStatus.BAD_REQUEST));
                log.error(message);
                return responseWriter;
            }

            if (Protocol.FTP.name().toLowerCase().equals(protocol) || Protocol.FTPS.name().toLowerCase().equals(protocol)) {
                return handleFtpConnection(connectionPayload);
            } else if (Protocol.HTTP.name().toLowerCase().equals(protocol)) {
                return handleHttpConnection(connectionPayload);
            }

        } catch (Exception ex) {
            log.error("Error processing connection creation", ex);
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return responseWriter;
    }

    private DeferredResult<ResponseEntity<String>> handleFtpConnection(Map<String, Object> connectionPayload) {
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();

        try {
            FTPConnection connection = objectMapper.convertValue(connectionPayload, FTPConnection.class);
            connection.setReceivedAt(LocalDateTime.now());

            // Validate
            ReecoRequestParamValidator<FTPConnection> validator = new ReecoRequestParamValidator<>();
            responseWriter = validator.getResponseMessage(connection);
            if (responseWriter.getResult() != null) {
                return responseWriter;
            }

            // Create Kafka message
            Map<String, String> headers = new HashMap<>();
            headers.put("protocol", connection.getProtocol().name());

            byte[] connectionBytes = objectMapper.writeValueAsString(connection).getBytes();
            ProducerRecord<String, byte[]> record = kafkaService.createKafkaRecord(
                    TOPIC_NAME,
                    connection.getOrganizationId().toString(),
                    connectionBytes,
                    ActionType.UPSERT,
                    EntityType.CONNECTION,
                    headers
            );

            return kafkaService.sendKafkaMessage(record);

        } catch (JsonProcessingException ex) {
            log.error("Error serializing FTP connection", ex);
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return responseWriter;
    }

    private DeferredResult<ResponseEntity<String>> handleHttpConnection(Map<String, Object> connectionPayload) {
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();

        try {
            HTTPConnection httpConnection = objectMapper.convertValue(connectionPayload, HTTPConnection.class);
            String accessToken = httpConnection.getAccessToken();
            httpConnection.setUpdatedAt(LocalDateTime.now());

            log.info("Processing HTTP connection with access token: {}", accessToken);

            // Encrypt the access token
            httpConnection.setAccessToken(AES.encrypt(accessToken));

            // Create Kafka message
            Map<String, String> headers = new HashMap<>();
            headers.put("protocol", httpConnection.getProtocol().name());

            byte[] connectionBytes = objectMapper.writeValueAsString(httpConnection).getBytes();
            ProducerRecord<String, byte[]> record = kafkaService.createKafkaRecord(
                    TOPIC_NAME,
                    httpConnection.getOrganizationId().toString(),
                    connectionBytes,
                    ActionType.UPSERT,
                    EntityType.CONNECTION,
                    headers
            );

            return kafkaService.sendKafkaMessage(record);

        } catch (JsonProcessingException ex) {
            log.error("Error serializing HTTP connection", ex);
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return responseWriter;
    }

    @Override
    public DeferredResult<ResponseEntity<String>> deleteFtpConnection(Long id, Long orgId) {
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        log.info("Deleting FTP connection - id: {}, orgId: {}", id, orgId);

        try {
            FTPConnection connection = new FTPConnection();
            connection.setOrganizationId(orgId);
            connection.setId(id);

            Map<String, String> headers = new HashMap<>();
            headers.put("protocol", Protocol.FTP.name());

            byte[] connectionBytes = objectMapper.writeValueAsString(connection).getBytes();
            ProducerRecord<String, byte[]> record = kafkaService.createKafkaRecord(
                    TOPIC_NAME,
                    orgId.toString(),
                    connectionBytes,
                    ActionType.DELETE,
                    EntityType.CONNECTION,
                    headers
            );

            return kafkaService.sendKafkaMessage(record);

        } catch (JsonProcessingException ex) {
            log.error("Error serializing FTP connection for deletion", ex);
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return responseWriter;
    }

    @Override
    public DeferredResult<ResponseEntity<String>> deleteHttpConnection(Long id, Long orgId) {
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        log.info("Deleting HTTP connection - id: {}, orgId: {}", id, orgId);

        try {
            HTTPConnection httpConnection = new HTTPConnection();
            httpConnection.setOrganizationId(orgId);
            httpConnection.setId(id);

            Map<String, String> headers = new HashMap<>();
            headers.put("protocol", Protocol.HTTP.name());

            byte[] connectionBytes = objectMapper.writeValueAsString(httpConnection).getBytes();
            ProducerRecord<String, byte[]> record = kafkaService.createKafkaRecord(
                    TOPIC_NAME,
                    orgId.toString(),
                    connectionBytes,
                    ActionType.DELETE,
                    EntityType.CONNECTION,
                    headers
            );

            return kafkaService.sendKafkaMessage(record);

        } catch (JsonProcessingException ex) {
            log.error("Error serializing HTTP connection for deletion", ex);
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return responseWriter;
    }
}