package com.reeco.ingestion.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.ingestion.application.usecase.EntityManagementUseCase;
import com.reeco.ingestion.infrastructure.queue.kafka.QueueConsumer;
import com.reeco.ingestion.utils.annotators.Adapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

import java.util.Map;



@RequiredArgsConstructor
@Slf4j
@Controller
public class EntityManagementController {

    private final EntityManagementUseCase entityManagementUseCase;

    private final ObjectMapper objectMapper;

//    @KafkaListener(topics = "reeco_config_event",containerFactory = "timeSeriesEventListener")
//    public void listen(Map<String, byte[]> header, ConsumerRecord<String, byte[]> message) {
//        // implement this
//    }
}
