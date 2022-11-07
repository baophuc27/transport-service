package com.reeco.config.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.common.model.dto.*;
import com.reeco.common.model.enumtype.ActionType;
import com.reeco.common.model.enumtype.CustomIdType;
import com.reeco.common.model.enumtype.EntityType;
import com.reeco.common.model.enumtype.Protocol;
import com.reeco.common.utils.AES;
import com.reeco.common.utils.Utils;
import com.reeco.config.service.ReecoRequestParamValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(value = {"*"})
@RestController()
@RequiredArgsConstructor
@Slf4j
class ConfigEntityController {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private KafkaTemplate<String, byte[]> producerTemplate;

    private final String TOPIC_NAME = "reeco_config_event";

    @PostMapping("config/parameter")
    private DeferredResult<ResponseEntity<String>> createParams(@RequestBody Parameter parameter) {

        log.info("parameter payload: {}", parameter);
        ReecoRequestParamValidator<Parameter> validator = new ReecoRequestParamValidator<>();
        DeferredResult<ResponseEntity<String>> responseWriter = validator.getResponseMessage(parameter);
        if (responseWriter.getResult() != null) {
            return responseWriter;
        }
        LocalDateTime currentTimestamp = LocalDateTime.now();
        parameter.setReceivedAt(currentTimestamp);
        ProducerRecord<String, byte[]> msg = null;
        try {
            msg = new ProducerRecord<>(TOPIC_NAME, parameter.getOrganizationId().toString(),
                    objectMapper.writeValueAsString(parameter).getBytes());
            msg.headers()
                    .add("actionType", ActionType.UPSERT.name().getBytes())
                    .add("entityType", EntityType.PARAM.name().getBytes());

            producerTemplate.send(msg).addCallback(new HttpOkCallback(responseWriter));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            responseWriter.setResult(new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return responseWriter;
    }

    @DeleteMapping("config/parameter/{orgId}/{connectionId}/{id}")
    private DeferredResult<ResponseEntity<String>> deleteParams(@PathVariable("id") Long id,
                                                                @PathVariable(value = "connectionId") Long connectionId,
                                                                @PathVariable(value = "orgId") Long orgId) {
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        LocalDateTime currentTimestamp = LocalDateTime.now();
        log.info("Delete Param: {} in org: {}", id, orgId);
        Parameter parameter = new Parameter();
        parameter.setId(id);
        parameter.setConnectionId(connectionId);
        parameter.setOrganizationId(orgId);
        parameter.setReceivedAt(currentTimestamp);
        ProducerRecord<String, byte[]> msg = null;
        try {
            msg = new ProducerRecord<>(TOPIC_NAME, orgId.toString(), objectMapper.writeValueAsString(parameter).getBytes());
            msg.headers()
                    .add("actionType", ActionType.DELETE.name().getBytes())
                    .add("entityType", EntityType.PARAM.name().getBytes());

            producerTemplate.send(msg).addCallback(new HttpOkCallback(responseWriter));

        } catch (JsonProcessingException e) {
            responseWriter.setResult(new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return responseWriter;
    }

    @PostMapping("config/api/api-key")
    private DeferredResult<ResponseEntity<String>> upsertAPIKey(
            @RequestBody Map<String, Object> apiKeyPayload
    ){

        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        log.info("Upsert API Key payload: {}", apiKeyPayload);
        try{
            APIKey apiKey = objectMapper.convertValue(apiKeyPayload, APIKey.class);

            ProducerRecord<String, byte[]> msg = new ProducerRecord<>(TOPIC_NAME, "1",
                    objectMapper.writeValueAsString(apiKeyPayload).getBytes()
                    );
            msg.headers()
                    .add("actionType", ActionType.UPSERT.name().getBytes())
                    .add("entityType", EntityType.APIKEY.name().getBytes());

            producerTemplate.send(msg).addCallback(new HttpOkCallback(responseWriter));

        } catch (IllegalArgumentException | JsonProcessingException ex){
            ex.printStackTrace();
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }
        return responseWriter;

    }

    @PostMapping("config/api/mqtt-sender")
    private DeferredResult<ResponseEntity<String>> upsertMQTTShare(
            @RequestBody Map<String, Object> mqttPayload
    ){

        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        log.info("Upsert MQTT Share payload: {}", mqttPayload);
        try{
            MQTTShare mqtt = objectMapper.convertValue(mqttPayload, MQTTShare.class);

            ProducerRecord<String, byte[]> msg = new ProducerRecord<>(TOPIC_NAME, "1",
                    objectMapper.writeValueAsString(mqtt).getBytes()
            );
            msg.headers()
                    .add("actionType", ActionType.UPSERT.name().getBytes())
                    .add("entityType", EntityType.MQTTSHARE.name().getBytes());

            producerTemplate.send(msg).addCallback(new HttpOkCallback(responseWriter));

        } catch (IllegalArgumentException | JsonProcessingException ex){
            ex.printStackTrace();
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }
        return responseWriter;

    }

    @DeleteMapping("config/api/mqtt-sender/{id}")
    private DeferredResult<ResponseEntity<String>> deleteMqttShare(
            @PathVariable(value = "id") String id
    ){
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();

        log.info("Delete MQTT Share: {}",id);
        MQTTShare mqtt = new MQTTShare();
        mqtt.setId(id);

        try{
            ProducerRecord<String, byte[]> msg = new ProducerRecord<>(TOPIC_NAME, "1", objectMapper.writeValueAsString(mqtt).getBytes());
            msg.headers()
                    .add("actionType",ActionType.DELETE.name().getBytes())
                    .add("entityType",EntityType.MQTTSHARE.name().getBytes());
            producerTemplate.send(msg).addCallback(new HttpOkCallback(responseWriter));
        }
        catch (JsonProcessingException ex){
//            ex.printStackTrace();
            log.warn("Error when delete mqtt share {}",ex.getMessage());
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return responseWriter;
    }
    @DeleteMapping("config/api/api-key/{id}")
    private DeferredResult<ResponseEntity<String>> deleteApiKey(
            @PathVariable(value = "id") String id
    ){
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();

        log.info("Delete API Key: {}",id);
        APIKey apiKey = new APIKey();
        apiKey.setId(id);

        try{
            ProducerRecord<String, byte[]> msg = new ProducerRecord<>(TOPIC_NAME, "1", objectMapper.writeValueAsString(apiKey).getBytes());
            msg.headers()
                    .add("actionType",ActionType.DELETE.name().getBytes())
                    .add("entityType",EntityType.APIKEY.name().getBytes());
            producerTemplate.send(msg).addCallback(new HttpOkCallback(responseWriter));
        }
        catch (JsonProcessingException ex){
//            ex.printStackTrace();
            log.warn("Error when delete api key {}",ex.getMessage());
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return responseWriter;
    }

    @PostMapping("config/connection/{protocol}")
    private DeferredResult<ResponseEntity<String>> createConnection(
            @PathVariable("protocol") String protocol,
            @RequestBody Map<String, Object> connectionPayload) {

        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        log.info("Connection payload: {}", connectionPayload);
        LocalDateTime currentTimestamp = LocalDateTime.now();

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
                FTPConnection connection = objectMapper.convertValue(connectionPayload, FTPConnection.class);
                connection.setReceivedAt(currentTimestamp);
                ReecoRequestParamValidator<Connection> validator = new ReecoRequestParamValidator<>();
                responseWriter = validator.getResponseMessage(connection);
                if (responseWriter.getResult() != null) {
                    return responseWriter;
                }

                ProducerRecord<String, byte[]> msg = new ProducerRecord<>(TOPIC_NAME, connection.getOrganizationId().toString(),
                        objectMapper.writeValueAsString(connection).getBytes());
                msg.headers()
                        .add("actionType", ActionType.UPSERT.name().getBytes())
                        .add("entityType", EntityType.CONNECTION.name().getBytes())
                        .add("protocol", connection.getProtocol().name().getBytes());

                producerTemplate.send(msg).addCallback(new HttpOkCallback(responseWriter));
            } else if (Protocol.HTTP.name().toLowerCase().equals(protocol)) {
                HTTPConnection httpConnection = objectMapper.convertValue(connectionPayload, HTTPConnection.class);
                String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$?";
                String access_token = httpConnection.getAccessToken();
                httpConnection.setUpdatedAt(currentTimestamp);
                log.info("Access_token: {}", access_token);
                httpConnection.setAccessToken(AES.encrypt(access_token));
                ProducerRecord<String, byte[]> msg = new ProducerRecord<>(TOPIC_NAME, httpConnection.getOrganizationId().toString(),
                        objectMapper.writeValueAsString(httpConnection).getBytes());
                msg.headers()
                        .add("actionType", ActionType.UPSERT.name().getBytes())
                        .add("entityType", EntityType.CONNECTION.name().getBytes())
                        .add("protocol", httpConnection.getProtocol().name().getBytes());

                producerTemplate.send(msg).addCallback(new HttpOkCallback(responseWriter));
//                responseWriter.setResult(new ResponseEntity<>(access_token, HttpStatus.OK));
            }


        } catch (IllegalArgumentException | JsonProcessingException ex) {
            ex.printStackTrace();
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }
        return responseWriter;
    }

    @PostMapping("config/customid")
    private DeferredResult<ResponseEntity<String>> createCustomId(
            @RequestBody CustomId customIdPayload
    ){
        log.info("Custom ID create payload: {}", customIdPayload);
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        LocalDateTime currentTimestamp = LocalDateTime.now();
        String customIdType = customIdPayload.getCustomIdType().name().toLowerCase();
        log.debug(customIdPayload.getCustomIdType().name().toLowerCase());
        try{
            if (!CustomIdType.CONNECTION.name().toLowerCase().equals(customIdType)
                && !CustomIdType.WORKSPACE.name().toLowerCase().equals(customIdType)
                && !CustomIdType.PARAMETER.name().toLowerCase().equals(customIdType)
                && !CustomIdType.ORGANIZATION.name().toLowerCase().equals(customIdType)
                && !CustomIdType.STATION.name().toLowerCase().equals(customIdType)){
                String message = "Unsupported custom Id type for " + customIdPayload.getCustomIdType();
                responseWriter.setResult(new ResponseEntity<>(message, HttpStatus.BAD_REQUEST));
                log.error(message);
                return responseWriter;
            }

//            if (!CustomIdType.)
            ReecoRequestParamValidator<CustomId> validator = new ReecoRequestParamValidator<>();
            responseWriter = validator.getResponseMessage(customIdPayload);

            if (responseWriter.getResult() != null){
                return responseWriter;
            }

            ProducerRecord<String, byte[]> msg = new ProducerRecord<>(TOPIC_NAME, customIdPayload.getOriginalId().toString(),
                    objectMapper.writeValueAsString(customIdPayload).getBytes());
            msg.headers()
                    .add("customIdType",customIdPayload.getCustomIdType().name().getBytes())
                    .add("actionType", ActionType.UPSERT.name().getBytes())
                    .add("entityType", EntityType.CUSTOMID.name().getBytes());

            producerTemplate.send(msg).addCallback(new HttpOkCallback(responseWriter));
        } catch (IllegalArgumentException | JsonProcessingException ex){
            ex.printStackTrace();
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return responseWriter;
    }

    @DeleteMapping("config/customid/{customIdType}/{id}")
    private DeferredResult<ResponseEntity<String>> deleteCustomId(
            @PathVariable(value = "customIdType") String idType,
            @PathVariable(value = "id") Integer originalId
    ){
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        String customIdType = idType.toUpperCase();
        log.info("Delete custom id: {} in {}", originalId,customIdType);


        try{
            if (!CustomIdType.CONNECTION.name().toUpperCase().equals(customIdType)
                    && !CustomIdType.WORKSPACE.name().toUpperCase().equals(customIdType)
                    && !CustomIdType.PARAMETER.name().toUpperCase().equals(customIdType)
                    && !CustomIdType.ORGANIZATION.name().toUpperCase().equals(customIdType)
                    && !CustomIdType.STATION.name().toUpperCase().equals(customIdType)){
                String message = "Unsupported custom Id type for " + customIdType;
                responseWriter.setResult(new ResponseEntity<>(message, HttpStatus.BAD_REQUEST));
                log.error(message);
                return responseWriter;
            }
            CustomId customIdPayload = new CustomId();
            customIdPayload.setOriginalId(String.valueOf(originalId));
            customIdPayload.setCustomIdType(CustomIdType.valueOf(customIdType));

            ProducerRecord<String, byte[]> msg = new ProducerRecord<>(TOPIC_NAME, String.valueOf(originalId),
                    objectMapper.writeValueAsString(customIdPayload).getBytes());
            msg.headers()
                    .add("actionType", ActionType.DELETE.name().getBytes())
                    .add("entityType", EntityType.CUSTOMID.name().getBytes());
            producerTemplate.send(msg).addCallback(new HttpOkCallback(responseWriter));

//
        } catch (IllegalArgumentException | JsonProcessingException ex){
            ex.printStackTrace();
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }

        return responseWriter;


    }

    @DeleteMapping("config/connection/{protocol}/{orgId}/{id}")
    private DeferredResult<ResponseEntity<String>> deleteConnection(@PathVariable(value = "id") Long id,
                                                                    @PathVariable(value = "protocol") String protocol,
                                                                    @PathVariable(value = "orgId") Long orgId) {
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        log.info("Delete connection: {} in station: {}", id, orgId);

        if (Protocol.FTP.name().toLowerCase().equals(protocol) || Protocol.FTPS.name().toLowerCase().equals(protocol)) {
            FTPConnection connection = new FTPConnection();
            connection.setOrganizationId(orgId);
            connection.setId(id);
            ProducerRecord<String, byte[]> msg = null;
            try {
                msg = new ProducerRecord<>(TOPIC_NAME, orgId.toString(), objectMapper.writeValueAsString(connection).getBytes());
                msg.headers()
                        .add("actionType", ActionType.DELETE.name().getBytes())
                        .add("entityType", EntityType.CONNECTION.name().getBytes())
                        .add("protocol", Protocol.FTP.name().getBytes());
                producerTemplate.send(msg).addCallback(new HttpOkCallback(responseWriter));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                responseWriter.setResult(new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST));
            }

        } else if (Protocol.HTTP.name().toLowerCase().equals(protocol)) {
            HTTPConnection httpConnection = new HTTPConnection();
            httpConnection.setOrganizationId(orgId);
            httpConnection.setId(id);
            ProducerRecord<String, byte[]> msg = null;
            try {
                msg = new ProducerRecord<>(TOPIC_NAME, orgId.toString(), objectMapper.writeValueAsString(httpConnection).getBytes());
                msg.headers()
                        .add("actionType", ActionType.DELETE.name().getBytes())
                        .add("entityType", EntityType.CONNECTION.name().getBytes())
                        .add("protocol", Protocol.HTTP.name().getBytes());
                producerTemplate.send(msg).addCallback(new HttpOkCallback(responseWriter));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                responseWriter.setResult(new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST));
            }
        } else {
            responseWriter.setResult(new ResponseEntity<>("Unsupported protocol " + protocol, HttpStatus.BAD_REQUEST));
        }

        return responseWriter;
    }


    private static class HttpOkCallback implements ListenableFutureCallback<SendResult<String, byte[]>> {

        private final DeferredResult<ResponseEntity<String>> responseWriter;

        public HttpOkCallback(DeferredResult<ResponseEntity<String>> responseWriter) {
            this.responseWriter = responseWriter;
        }

        @Override
        public void onFailure(Throwable e) {
            responseWriter.setResult(new ResponseEntity<>("send failure!, please try again!", HttpStatus.INTERNAL_SERVER_ERROR));
            log.error("unable to send message ", e);
        }

        @Override
        public void onSuccess(SendResult result) {
            responseWriter.setResult(new ResponseEntity<>("send success!", HttpStatus.OK));
            if (result != null) {
                log.info(result + " with offset= " + result.getRecordMetadata().offset());
            }
        }
    }

    public Map<String, byte[]> buildHeaders(ActionType actionType, EntityType entityType) {
        Map<String, byte[]> headers = new HashMap<>();
        headers.put("actionType", actionType.name().getBytes());
        headers.put("entityType", entityType.name().getBytes());
        return headers;
    }

}