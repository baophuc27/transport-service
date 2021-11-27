package com.reeco.config.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.common.model.dto.BaseConnection;
import com.reeco.common.model.dto.Connection;
import com.reeco.common.model.dto.FTPConnection;
import com.reeco.common.model.dto.Parameter;
import com.reeco.common.model.enumtype.ActionType;
import com.reeco.common.model.enumtype.EntityType;
import com.reeco.common.model.enumtype.Protocol;
import com.reeco.common.utils.Utils;
import com.reeco.config.service.ReecoRequestParamValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private DeferredResult<ResponseEntity<String>> createParams(@RequestBody Parameter parameterDTO) {

        log.info("parameter payload: {}", parameterDTO);
        ReecoRequestParamValidator<Parameter> validator = new ReecoRequestParamValidator<>();
        DeferredResult<ResponseEntity<String>> responseWriter = validator.getResponseMessage(parameterDTO);
        if (responseWriter.getResult() != null) {
            return responseWriter;
        }
        LocalDateTime currentTimestamp = LocalDateTime.now();
        parameterDTO.setReceivedAt(currentTimestamp.format(formatter));
        ProducerRecord<String, byte[]> msg = new ProducerRecord<>(TOPIC_NAME, parameterDTO.getOrganizationId().toString(), Utils.getBytes(parameterDTO));
        msg.headers()
                .add("actionType", ActionType.UPSERT.name().getBytes())
                .add("entityType", EntityType.PARAM.name().getBytes());

        producerTemplate.send(msg).addCallback(new HttpOkCallback(responseWriter));

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
        parameter.setReceivedAt(currentTimestamp.format(formatter));
        ProducerRecord<String, byte[]> msg = new ProducerRecord<>(TOPIC_NAME, orgId.toString(), Utils.getBytes(parameter));
        msg.headers()
                .add("actionType", ActionType.DELETE.name().getBytes())
                .add("entityType", EntityType.PARAM.name().getBytes());

        producerTemplate.send(msg).addCallback(new HttpOkCallback(responseWriter));

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
            if (!Protocol.FTP.name().toLowerCase().equals(protocol)) {
                responseWriter.setResult(new ResponseEntity<>("Unsupported connection method " + protocol, HttpStatus.BAD_REQUEST));
                return responseWriter;
            }
            FTPConnection connection = objectMapper.convertValue(connectionPayload, FTPConnection.class);
            connection.setReceivedAt(currentTimestamp.format(formatter));
            ReecoRequestParamValidator<Connection> validator = new ReecoRequestParamValidator<>();
            responseWriter = validator.getResponseMessage(connection);
            if (responseWriter.getResult() != null) {
                return responseWriter;
            }

            ProducerRecord<String, byte[]> msg = new ProducerRecord<>(TOPIC_NAME, connection.getOrganizationId().toString(), Utils.getBytes(connection));
            msg.headers()
                    .add("actionType", ActionType.UPSERT.name().getBytes())
                    .add("entityType", EntityType.CONNECTION.name().getBytes())
                    .add("protocol", connection.getProtocol().name().getBytes());

            producerTemplate.send(msg).addCallback(new HttpOkCallback(responseWriter));


        } catch (IllegalArgumentException ex) {
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
        if (Protocol.FTP.name().toLowerCase().equals(protocol)){
            FTPConnection connection = new FTPConnection();
            connection.setOrganizationId(orgId);
            connection.setId(id);
            ProducerRecord<String, byte[]> msg = new ProducerRecord<>(TOPIC_NAME, orgId.toString(), Utils.getBytes(connection));
            msg.headers()
                    .add("actionType", ActionType.DELETE.name().getBytes())
                    .add("entityType", EntityType.CONNECTION.name().getBytes())
                    .add("protocol",Protocol.FTP.name().getBytes());
            producerTemplate.send(msg).addCallback(new HttpOkCallback(responseWriter));
        }
        else {
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

    public Map<String, byte[]> buildHeaders(ActionType actionType, EntityType entityType){
        Map<String, byte[]> headers = new HashMap<>();
        headers.put("actionType", actionType.name().getBytes());
        headers.put("entityType", entityType.name().getBytes());
        return headers;
    }

}