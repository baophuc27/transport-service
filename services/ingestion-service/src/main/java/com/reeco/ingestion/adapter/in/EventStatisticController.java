package com.reeco.ingestion.adapter.in;

import com.reeco.ingestion.application.port.in.IncomingTsEvent;
//import com.reeco.ingestion.application.usecase.UpdateStatEventUseCase;
import com.reeco.ingestion.application.usecase.StatisticEventUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Controller
@RequiredArgsConstructor
public class EventStatisticController {

    @Autowired
    private KafkaTemplate<String, IncomingTsEvent> kafkaProducerEventTemplate;

    @Autowired
    private final StatisticEventUseCase updateStatEventUseCase;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Scheduled(fixedRate = 120000)
    public void scheduleTaskWithFixedRate() {
        LocalDateTime endTime = LocalDateTime.now();
        updateStatEventUseCase.updateNumStatEvent(endTime);
    }

    @Scheduled(fixedRate = 60000)
    public void kafkaProducerMessage() {
        System.out.println("Implement this!");
        // send message to kafka topic
        // payload: IncomingTsEvent
        // random data send to kafka topic: reeco_time_series_event
        // kafkaProducerEventTemplate.send();
    }

    @Scheduled(fixedRate = 1500)
    public void scheduleTaskWithFixedRate2() {
        log.info("Fixed Rate Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()) );
    }

    @Scheduled(cron = "0 * * * * ?")
    public void scheduleTaskWithCronExpression() {
        log.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
    }
//
//    @Scheduled(cron = "0 * * * * ?")
//    public void updateNumStatisticEvent() {
//        numericStatEventUseCase.updateNumStatEvent();
//    }
//
//    @Scheduled(cron = "0 * * * * ?")
//    public void updateCatStatisticEvent() {
//        numericStatEventUseCase.updateCatStatEvent();
//    }
}
