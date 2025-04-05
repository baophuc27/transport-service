package com.reeco.config.service.impl;

import com.reeco.common.model.enumtype.ActionType;
import com.reeco.common.model.enumtype.EntityType;
import com.reeco.config.service.KafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaServiceImpl implements KafkaService {
    private final KafkaTemplate<String, byte[]> producerTemplate;

    @Override
    public DeferredResult<ResponseEntity<String>> sendKafkaMessage(ProducerRecord<String, byte[]> message) {
        DeferredResult<ResponseEntity<String>> responseWriter = new DeferredResult<>();

        producerTemplate.send(message).addCallback(new ListenableFutureCallback<SendResult<String, byte[]>>() {
            @Override
            public void onFailure(Throwable e) {
                log.error("Unable to send message to Kafka", e);
                responseWriter.setResult(new ResponseEntity<>("Failed to process request. Please try again.",
                        HttpStatus.INTERNAL_SERVER_ERROR));
            }

            @Override
            public void onSuccess(SendResult<String, byte[]> result) {
                if (result != null) {
                    log.info("Message sent successfully with offset: {}", result.getRecordMetadata().offset());
                }
                responseWriter.setResult(new ResponseEntity<>("Request processed successfully", HttpStatus.OK));
            }
        });

        return responseWriter;
    }

    @Override
    public ProducerRecord<String, byte[]> createKafkaRecord(String topic, String key, byte[] value,
                                                            ActionType actionType, EntityType entityType,
                                                            Map<String, String> additionalHeaders) {
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, key, value);

        // Add required headers
        record.headers().add("actionType", actionType.name().getBytes());
        record.headers().add("entityType", entityType.name().getBytes());

        // Add any additional headers
        if (additionalHeaders != null) {
            additionalHeaders.forEach((headerKey, headerValue) ->
                    record.headers().add(headerKey, headerValue.getBytes())
            );
        }

        return record;
    }
}