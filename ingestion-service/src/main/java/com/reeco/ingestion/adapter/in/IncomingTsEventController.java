package com.reeco.ingestion.adapter.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.ingestion.application.port.in.IncomingTsEvent;
import com.reeco.ingestion.application.port.in.IncomingConfigEvent;
import com.reeco.ingestion.application.usecase.StoreConfigUseCase;
import com.reeco.ingestion.application.usecase.StoreTsEventUseCase;
import com.reeco.ingestion.cache.service.AlarmCacheUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IncomingTsEventController {

    private final StoreTsEventUseCase storeTsEventUseCase;

    private final StoreConfigUseCase storeConfigUseCase;

    private final AlarmCacheUseCase alarmCacheUseCase;




    @KafkaListener(topics = "reeco_time_series_event",containerFactory = "timeSeriesEventListener")
    public void listen(@Headers Map<String,byte[]> header, @Payload IncomingTsEvent message){
        storeTsEventUseCase.storeEvent(message);
    }

    @KafkaListener(topics = "reeco_config_event", containerFactory = "configEventListener")
    public void listen(@Headers Map<String, byte[]> header, @Payload String config) throws JsonProcessingException {



        ObjectMapper mapper = new ObjectMapper();
        IncomingConfigEvent incomingConfigEvent  = mapper.readValue(config, IncomingConfigEvent.class);
        storeConfigUseCase.storeConfig(incomingConfigEvent);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        alarmCacheUseCase.loadDataToCache();
        log.info("Load some data to Cache!");
    }
}
