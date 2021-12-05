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
import java.util.List;
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
        updateStatEventUseCase.updateNumStatEvent(endTime);
    }

    @Scheduled(fixedRate = 6000)
    public void kafkaProducerMessage() {
        Random rand = new Random();
        LocalDateTime currTime = LocalDateTime.now();
        List<Long> paramIdTest = Arrays.asList(24L,25L,28L);
        List<String> indicatorName = Arrays.asList("Temperature", "Wind Direction", "Salinity");
        List<Long> indicatorTest = Arrays.asList(1L,255L,223L);
        List<String> paramNames = Arrays.asList("nhiet do test-data",
                "do man test-data",
                "huong-gio test-data");
        List<String> windDirectionValues = Arrays.asList("N", "N/NE","NE","E/NE","E",
                "E/SE","SE","S/SE","S","S/SW",
                "SW","W/SW","W","W/NW","NW","N/NW");
        int idx = rand.nextInt(3);

        IncomingTsEvent msg = new IncomingTsEvent();

        msg.setOrganizationId(3L);
        msg.setWorkspaceId(9L);
        msg.setStationId(8L);
        msg.setConnectionId(21L);
        msg.setParamId(paramIdTest.get(idx));
        msg.setEventTime(currTime);
        msg.setIndicatorId(indicatorTest.get(idx));
        msg.setIndicatorName(indicatorName.get(idx));
        msg.setParamName(paramNames.get(idx));
        if (paramIdTest.get(idx) == 24L){
            msg.setValue(randomDouble(20L, 30L).toString());
        }
        else if (paramIdTest.get(idx) == 25L){
            msg.setValue(randomDouble(0.05, 0.09).toString());
        }
        else msg.setValue(windDirectionValues.get(rand.nextInt(windDirectionValues.size())));
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

    public Double randomDouble(double leftLimit, double rightLimit){
        Random r = new Random();
        return leftLimit + (rightLimit - leftLimit) * r.nextDouble();
    }
//
//    @Scheduled(cron = "0 * * * * ?")
//    public void scheduleTaskWithCronExpression() {
//        log.info("Cron Task :: Execution Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
//    }

}
