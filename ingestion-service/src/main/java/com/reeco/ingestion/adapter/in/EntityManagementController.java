package com.reeco.ingestion.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
@Controller
public class EntityManagementController {

    private ObjectMapper objectMapper;

    @KafkaListener(topics = "reeco_config_event",containerFactory = "configEventListener")
    public void listen(byte[] header, ConsumerRecord<String, byte[]> message) {

        System.out.println(message);
        System.out.println(header);
    }


    private <T> T parseObject(byte[] message, Class<T> valueType){
        try {
            return objectMapper.readValue(message,valueType);
        } catch (RuntimeException | IOException e) {
            log.warn("Error when parsing message object: {}",e.getMessage());
            return null;
        }
    }
}
