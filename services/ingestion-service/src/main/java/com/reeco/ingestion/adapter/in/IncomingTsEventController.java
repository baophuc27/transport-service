package com.reeco.ingestion.adapter.in;

import com.reeco.ingestion.application.port.in.IncomingTsEvent;
import com.reeco.ingestion.application.usecase.StoreTsEventUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @KafkaListener(topics = "reeco_time_series_event",containerFactory = "timeSeriesEventListener")
    public void listen(@Headers Map<String,byte[]> header, @Payload IncomingTsEvent message){
        storeTsEventUseCase.storeEvent(message);
    }
}
