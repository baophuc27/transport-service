package com.reeco.ingestion.adapter.in;

import com.reeco.ingestion.application.port.in.IncomingTsEvent;
import com.reeco.ingestion.application.usecase.StatisticEventUseCase;
import com.reeco.ingestion.cache.model.AlarmCache;
import com.reeco.ingestion.cache.service.AlarmCacheUseCase;
import com.reeco.ingestion.cache.service.IndicatorCacheUseCase;
import com.reeco.ingestion.domain.Indicator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;

@Slf4j
@Controller
@RequiredArgsConstructor
public class EventStatisticController {

    @Autowired
    private KafkaTemplate<String, IncomingTsEvent> kafkaProducerEventTemplate;

    @Autowired
    private final StatisticEventUseCase updateStatEventUseCase;

    @Autowired
    private final AlarmCacheUseCase alarmCacheUseCase;

    @Autowired
    private final IndicatorCacheUseCase indicatorCacheUseCase;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Scheduled(fixedRate = 10000)
    public void aggStatisticEvent() {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.MIN;
        LocalDateTime endTime = LocalDateTime.of(date, time).atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDateTime();
        LocalDateTime startTime = endTime.minusDays(5);
        Timestamp timestampEnd = Timestamp.valueOf(endTime);
        Timestamp timestampStart = Timestamp.valueOf(startTime);
        log.info("Start aggregation job with time range from {} to {}", startTime.toString(), endTime.toString());
        updateStatEventUseCase.updateNumStatEvent(timestampStart, timestampEnd);
    }

    @Scheduled(fixedRate = 60000)
    public void kafkaProducerMessage() {
        LocalDateTime currTime = LocalDateTime.now();
        IncomingTsEvent msg = new IncomingTsEvent();

        msg.setOrganizationId(generateRandomLong(1L, 4L));
        msg.setWorkspaceId(generateRandomLong(1L, 4L));
        msg.setStationId(generateRandomLong(1L, 3L));
        msg.setConnectionId(generateRandomLong(1L, 3L));
        msg.setParamId(generateRandomLong(1L, 11L));
        msg.setEventTime(currTime);
        msg.setIndicatorId(generateRandomLong(1L, 2L));
        msg.setIndicatorName("temp");
        msg.setParamName("nhiet do");
        msg.setValue(randomDouble(30L, 40L).toString());
        msg.setReceivedAt(currTime);
        msg.setSentAt(currTime);
        msg.setLat(new Random().nextDouble());
        msg.setLon(new Random().nextDouble());

        log.info(msg.toString());
        kafkaProducerEventTemplate.send("reeco_time_series_event", msg);
    }

    public Long generateRandomLong(Long leftLimit, Long rightLimit) {
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }

    public Double randomDouble(Long leftLimit, Long rightLimit){
        Random r = new Random();
        return leftLimit + (rightLimit - leftLimit) * r.nextDouble();
    }
//
//    @Scheduled(cron = "0 * * * * ?")
//    public void scheduleTaskWithCronExpression() {
//        log.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
//    }

}
