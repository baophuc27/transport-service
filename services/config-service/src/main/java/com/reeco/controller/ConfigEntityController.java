package com.reeco.controller;

import com.reeco.event.AlarmRequestMsg;
import com.reeco.event.ConnectionRequestMsg;
import com.reeco.event.ParametersRequestMsg;
import com.reeco.exception.ReecoException;
import com.reeco.framework.SendMessageRequest;
import com.reeco.model.*;
import com.reeco.model.dto.AlarmDTO;
import com.reeco.model.dto.ParameterDTO;
import com.reeco.model.dto.FTPConnectionDTO;
import com.reeco.service.ReecoRequestParamValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.validation.Validation;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(value = {"*"})
@RestController
@RequiredArgsConstructor
@Slf4j
class ConfigEntityController {

    private final SendMessageRequest sendMessageRequest;

    @PostMapping("/parameter/{orgId}/{connectionId}")
    private DeferredResult<ResponseEntity<String>> createParams(@PathVariable("orgId") Long ordId,
                                                                @PathVariable("connectionId") Long connectionId,
                                                                @RequestBody ParameterDTO parameterDTO) {

        log.info("parameter payload: {}", parameterDTO);

        ReecoRequestParamValidator<ParameterDTO> validator = new ReecoRequestParamValidator<>();
        DeferredResult<ResponseEntity<String>> responseWriter = validator.getResponseMessage(parameterDTO);
        if (responseWriter.getResult() != null) {
            return responseWriter;
        }

        long currentTimestamp = System.currentTimeMillis();

        ParametersRequestMsg requestMsg = new ParametersRequestMsg(
                currentTimestamp,
                ordId,
                connectionId,
                ActionType.UPSERT,
                EntityType.PARAM,
                parameterDTO);

        sendMessageRequest.sendMessage(requestMsg, new HttpOkCallback(responseWriter));

        return responseWriter;
    }

    @DeleteMapping("/parameter/{orgId}/{id}")
    private DeferredResult<ResponseEntity<String>> deleteParams(@PathVariable("id") Long id,
                                                                @PathVariable(value = "orgId") Long orgId) {
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        long currentTimestamp = System.currentTimeMillis();
        log.info("Delete Param: {} in org: {}", id, orgId);
        ParametersRequestMsg requestMsg = new ParametersRequestMsg(
                currentTimestamp,
                orgId,
                null,
                ActionType.DELETE,
                EntityType.PARAM,
                new ParameterDTO(id));

        sendMessageRequest.sendMessage(requestMsg, new HttpOkCallback(responseWriter));

        return responseWriter;
    }


    @PostMapping("/connection/{orgId}")
    private DeferredResult<ResponseEntity<String>> createConnection(@PathVariable(value = "orgId") Long orgId,
                                                                    @RequestBody Map<String, Object> connectionPayload) {

        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        log.info("Connection payload: {}", connectionPayload);
        long currentTimestamp = System.currentTimeMillis();

        try {
            String protocol = (String) connectionPayload.get("protocol");
            Connection connection = EntityModelFactory.getConnectionDTOByProtocol(protocol, connectionPayload);

            ReecoRequestParamValidator<Connection> validator = new ReecoRequestParamValidator<>();
            responseWriter = validator.getResponseMessage(connection);
            if (responseWriter.getResult() != null) {
                return responseWriter;
            }

            ConnectionRequestMsg requestMsg = new ConnectionRequestMsg(
                    currentTimestamp,
                    orgId,
                    ActionType.UPSERT,
                    EntityType.CONNECTION,
                    connection);
            sendMessageRequest.sendMessage(requestMsg, new HttpOkCallback(responseWriter));
        } catch (IllegalArgumentException | ReecoException ex) {
            responseWriter.setResult(new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST));
        }
        return responseWriter;
    }

    @DeleteMapping("/connection/{orgId}/{id}")
    private DeferredResult<ResponseEntity<String>> deleteConnection(@PathVariable(value = "id") Long id,
                                                                    @PathVariable(value = "orgId") Long orgId) {

        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();
        log.info("Delete connection: {} in station: {}", id, orgId);
        long currentTimestamp = System.currentTimeMillis();

        ConnectionRequestMsg requestMsg = new ConnectionRequestMsg(
                currentTimestamp,
                orgId,
                ActionType.DELETE,
                EntityType.CONNECTION,
                new FTPConnectionDTO(id));

        sendMessageRequest.sendMessage(requestMsg, new HttpOkCallback(responseWriter));

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

}