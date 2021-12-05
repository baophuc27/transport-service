package com.reeco.ingestion.adapter.out;

import com.datastax.oss.driver.shaded.guava.common.math.StatsAccumulator;
import com.reeco.common.model.dto.Alarm;
import com.reeco.common.model.dto.Parameter;
import com.reeco.ingestion.application.mapper.NumStatEventMapper;
import com.reeco.ingestion.application.mapper.NumericEventMapper;
import com.reeco.ingestion.application.mapper.ParameterMapper;
import com.reeco.ingestion.application.port.out.NumStatRepository;
import com.reeco.ingestion.application.usecase.RuleEngineUseCase;
import com.reeco.ingestion.cache.service.AlarmCacheUseCase;
import com.reeco.ingestion.domain.NumericalStatEvent;
import com.reeco.ingestion.domain.OrgAndParam;
import com.reeco.ingestion.domain.ParamAndAlarm;
import com.reeco.ingestion.domain.ParamsAndOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.EventStatisticMetaInfo;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumericalTsByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.ParamsByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.EventStatisticInfoRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.NumericalStatByOrgRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.NumericalTsByOrgRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.ParamsByOrgRepository;
import com.reeco.ingestion.utils.annotators.Adapter;
import com.reeco.ingestion.application.port.out.AggregateEventsPort;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;

@Adapter
@Log4j2
public class EventStatPersistenceAdapter implements AggregateEventsPort, NumStatRepository {

    @Autowired
    private ParamsByOrgRepository paramsByOrgRepository;

    @Autowired
    private EventStatisticInfoRepository eventStatisticInfoRepository;

    @Autowired
    private NumericalTsByOrgRepository numericalTsByOrgRepository;

    @Autowired
    private NumericalStatByOrgRepository numericalStatByOrgRepository;

    @Autowired
    private NumericEventMapper numericEventMapper;

    @Autowired
    private NumStatEventMapper numStatEventMapper;

    @Autowired
    private ParameterMapper parameterMapper;

    @Autowired
    private AlarmCacheUseCase alarmCacheUseCase;

    @Autowired
    private RuleEngineUseCase ruleEngineUseCase;

    private final LocalDateTime defaultDate = LocalDateTime
            .now()
            .atZone(ZoneId.of("Asia/Ho_Chi_Minh"))
            .toLocalDateTime().minusDays(1L);

    @Override
    public Flux<NumericalStatEvent> aggEventByOrgAndParams(LocalDateTime endTime) {
        return findAllParams()
                .log()
                .flatMap(v -> numericalTsByOrgRepository.finAllEventByOrg(
                        v.getPartitionKey().getOrganizationId(),
                        v.getPartitionKey().getParamId(),
                        Timestamp.valueOf(v.getLastAggTime()),
                        Timestamp.valueOf(endTime)
                        )
                                .groupBy(e -> new OrgAndParam(e.getPartitionKey().getOrganizationId(),
                                        e.getPartitionKey().getParamId(), e.getDate()))
                                .flatMap(gr -> gr
                                        .map(NumericalTsByOrg::getValue)
                                        .buffer()
                                        .map(k -> {
                                            Collections.sort(k);
                                            log.info(gr.key() + " - " + k.size());
                                            StatsAccumulator statsAccumulator = new StatsAccumulator();
                                            statsAccumulator.addAll(k);
                                            double mean = statsAccumulator.mean();
                                            NumericalStatEvent numericalStatEvent = new NumericalStatEvent(
                                                    gr.key().getOrganizationId(),
                                                    gr.key().getParamId(),
                                                    gr.key().getDate(),
                                                    statsAccumulator.min(),
                                                    statsAccumulator.max(),
                                                    mean,
                                                    statsAccumulator.sum(),
                                                    median(k),
                                                    statsAccumulator.count(),
                                                    statsAccumulator.populationStandardDeviation(),
                                                    null, null, null, null, null,
                                                    LocalDateTime.now()
                                            );
                                            ParamAndAlarm paramAndAlarm = alarmCacheUseCase
                                                    .get(gr.key().getParamId().toString());
                                            if (paramAndAlarm != null) {
                                                List<Alarm> alarms = paramAndAlarm.getAlarms();
                                                for (Alarm alarm : alarms) {
                                                    boolean isAlarm = ruleEngineUseCase.checkMatchingAlarmCondition(alarm, mean);
                                                    numericalStatEvent.setIsAlarm(isAlarm);
                                                    numericalStatEvent.setAlarmId(alarm.getId());
                                                    numericalStatEvent.setMinValue(alarm.getMinValue());
                                                    numericalStatEvent.setMaxValue(alarm.getMaxValue());
                                                    numericalStatEvent.setAlarmType(alarm.getAlarmType());
                                                    if (isAlarm) break;
                                                }
                                            }
                                            return numericalStatEvent;
                                        })
                                )
                );
    }

    private Double median(List<Double> sortedArr) {
        int middle = sortedArr.size() / 2;
        middle = middle > 0 && middle % 2 == 0 ? middle - 1 : middle;
        return sortedArr.get(middle);
    }

    @Override
    public void insert(LocalDateTime endTime) {
        aggEventByOrgAndParams(endTime)
                .map(v -> numStatEventMapper.toPersistence(v))
                .flatMap(v -> numericalStatByOrgRepository.save(v))
                .log()
                .flatMap(v->eventStatisticInfoRepository.save(
                        new EventStatisticMetaInfo(new EventStatisticMetaInfo.Key(v.getPartitionKey().getOrganizationId(),
                                v.getPartitionKey().getParamId()),
                                LocalDateTime.now(),
                                LocalDateTime.now().minusDays(1L))
                ))
                .subscribe(v -> log.info("Update meta info: " + v.toString()));

    }

    public Flux<EventStatisticMetaInfo> findAllParams() {
        return eventStatisticInfoRepository
                .findAll();
    }
}
