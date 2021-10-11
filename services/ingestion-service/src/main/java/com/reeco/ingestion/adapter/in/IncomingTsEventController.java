package com.reeco.ingestion.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.ingestion.application.port.in.IncomingTsEvent;
import com.reeco.ingestion.application.usecase.StoreTsEventUseCase;
import com.reeco.ingestion.domain.NumericTsEvent;
import com.reeco.ingestion.infrastructure.queue.kafka.QueueConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IncomingTsEventController implements QueueConsumer {

    private final StoreTsEventUseCase storeTsEventUseCase;

    private final ObjectMapper objectMapper;


    private <T> T parseObject(byte[] message, Class<T> valueType){
        // TODO: Change to msg queue deserializer
        try {
            return objectMapper.readValue(message,valueType);
        } catch (RuntimeException | IOException e) {
            log.warn("Error when parsing message object: {}",e.getMessage());
            return null;
        }
    }

    @Override
    @KafkaListener(topics = "reeco_time_series_event",containerFactory = "timeSeriesEventListener")
    public void listen(@Headers Map<String,byte[]> header, @Payload ConsumerRecord<String,byte[]> message){
        IncomingTsEvent timeSeriesEvent = parseObject(message.value(), IncomingTsEvent.class);
        storeTsEventUseCase.storeEvent(timeSeriesEvent);
    }
}
