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

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Slf4j
@Controller
@RequiredArgsConstructor
public class EventStatisticController {

    @Autowired
    private KafkaTemplate<String, IncomingTsEvent> kafkaProducerEventTemplate;

    @Autowired
    private final StatisticEventUseCase updateStatEventUseCase;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Scheduled(fixedRate = 12000)
    public void aggStatisticEvent() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.MIN;
        LocalDateTime endTime = LocalDateTime.of(date, time).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();
        LocalDateTime startTime = endTime.minusDays(40);
        Timestamp timestampEnd = Timestamp.valueOf(endTime);
        Timestamp timestampStart = Timestamp.valueOf(startTime);
        log.info("Start aggregation job with time range from {} to {}", startTime.toString(), endTime.toString());
        updateStatEventUseCase.updateNumStatEvent(timestampStart, timestampEnd);
    }

    @Scheduled(fixedRate = 30000)
    public void kafkaProducerMessage() {
        LocalDateTime currTime = LocalDateTime.now();
        IncomingTsEvent msg = new IncomingTsEvent();

        msg.setOrganizationId(generateRandomLong(1L, 6L));
        msg.setStationId(generateRandomLong(1L, 3L));
        msg.setConnectionId(generateRandomLong(1L, 3L));
        msg.setParamId(generateRandomLong(1L, 2L));
        msg.setEventTime(currTime);
        msg.setIndicatorId(generateRandomLong(1L, 2L));
        msg.setIndicatorName("temp");
        msg.setParamName("nhiet do");
        msg.setValue("30.00");
        msg.setReceivedAt(currTime);
        msg.setSentAt(currTime);
        msg.setLat(new Random().nextDouble());
        msg.setLon(new Random().nextDouble());

        log.info(String.valueOf(msg));
        kafkaProducerEventTemplate.send("reeco_time_series_event", msg);
    }

    public Long generateRandomLong(Long leftLimit, Long rightLimit) {
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }

    @Scheduled(cron = "0 * * * * ?")
    public void scheduleTaskWithCronExpression() {
        log.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
    }

}
