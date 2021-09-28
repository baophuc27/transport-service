package com.reeco.controller;

import com.reeco.event.ConnectionRequestMsg;
import com.reeco.event.ParametersRequestMsg;
import com.reeco.exception.ReecoException;
import com.reeco.framework.SendMessageRequest;
import com.reeco.model.*;
import com.reeco.model.dto.AttributeDTO;
import com.reeco.model.dto.FTPConnectionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;

@RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(value = { "*" })
@RestController
@RequiredArgsConstructor
@Slf4j
class DeviceController {

    private final SendMessageRequest sendMessageRequest;

    @PostMapping("/attributes/{stationId}/{connectionId}")
    private DeferredResult<ResponseEntity> createParams(@PathVariable("stationId") Long stationId,
                                                        @PathVariable("connectionId") Long connectionId,
                                                        @RequestBody Map<String, Object> attributePayload) {

        DeferredResult<ResponseEntity> responseWriter = new DeferredResult<>();
        log.info("Attribute payload: {}", attributePayload);
        try {
            String parameterType = (String) attributePayload.get("parameterType");
            Attribute parameter = EntityModelFactory.getParameterDTOByAttributeType(parameterType, attributePayload);

            long currentTimestamp = System.currentTimeMillis();

            ParametersRequestMsg requestMsg = new ParametersRequestMsg(
                    currentTimestamp,
                    stationId,
                    connectionId,
                    ActionType.UPSERT,
                    EntityType.ATTRIBUTE,
                    parameter);

            sendMessageRequest.sendMessage(requestMsg, new HttpOkCallback(responseWriter));
        }
        catch (IllegalArgumentException|ReecoException ex){
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }
        return responseWriter;
    }

    @DeleteMapping("/attributes/{stationId}/{id}")
    private DeferredResult<ResponseEntity> deleteParams(@PathVariable("id") Long id,
                                                        @PathVariable(value = "stationId") Long stationId) {
        DeferredResult<ResponseEntity> responseWriter = new DeferredResult<>();
        long currentTimestamp = System.currentTimeMillis();
        log.info("Delete attribute: {} in station: {}", id, stationId);
        ParametersRequestMsg requestMsg = new ParametersRequestMsg(
                currentTimestamp,
                stationId,
                null,
                ActionType.DELETE,
                EntityType.ATTRIBUTE,
                new AttributeDTO(id));

        sendMessageRequest.sendMessage(requestMsg, new HttpOkCallback(responseWriter));

        return responseWriter;
    }


    @PostMapping("/connection/{stationId}")
    private DeferredResult<ResponseEntity> createConnection(@PathVariable(value = "stationId", required = true) Long stationId,
                                                            @RequestBody Map<String, Object> connectionPayload) {

        DeferredResult<ResponseEntity> responseWriter = new DeferredResult<>();
        log.info("Connection payload: {}", connectionPayload);
        long currentTimestamp = System.currentTimeMillis();

        try {
            String protocol = (String) connectionPayload.get("protocol");
            Connection connection = EntityModelFactory.getConnectionDTOByProtocol(protocol, connectionPayload);
            ConnectionRequestMsg requestMsg = new ConnectionRequestMsg(
                    currentTimestamp,
                    stationId,
                    ActionType.UPSERT,
                    EntityType.CONNECTION,
                    connection);
            sendMessageRequest.sendMessage(requestMsg, new HttpOkCallback(responseWriter));
        }
        catch (IllegalArgumentException|ReecoException ex){
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }
        return responseWriter;
    }

    @DeleteMapping("/connection/{stationId}/{id}")
    private DeferredResult<ResponseEntity> deleteConnection(@PathVariable(value = "id") Long id,
                                                            @PathVariable(value = "stationId") Long stationId) {

        DeferredResult<ResponseEntity> responseWriter = new DeferredResult<>();
        log.info("Delete connection: {} in station: {}", id, stationId);
        long currentTimestamp = System.currentTimeMillis();

        ConnectionRequestMsg requestMsg = new ConnectionRequestMsg(
                currentTimestamp,
                stationId,
                ActionType.DELETE,
                EntityType.CONNECTION,
                new FTPConnectionDTO(id));

        sendMessageRequest.sendMessage(requestMsg, new HttpOkCallback(responseWriter));

        return responseWriter;
    }

    private static class HttpOkCallback implements ListenableFutureCallback<SendResult<String, byte[]>> {

        private final DeferredResult<ResponseEntity> responseWriter;

        public HttpOkCallback(DeferredResult<ResponseEntity> responseWriter) {
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

}