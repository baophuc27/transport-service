package com.reeco.ingestion.adapter.out;

import com.datastax.oss.driver.shaded.guava.common.math.StatsAccumulator;
import com.reeco.ingestion.application.mapper.NumStatEventMapper;
import com.reeco.ingestion.application.mapper.NumericEventMapper;
import com.reeco.ingestion.application.mapper.ParameterMapper;
import com.reeco.ingestion.application.port.out.AggregateEventsPort;
import com.reeco.ingestion.application.port.out.NumStatRepository;
import com.reeco.ingestion.domain.NumericalStatEvent;
import com.reeco.ingestion.domain.NumericalTsEvent;
import com.reeco.ingestion.domain.OrgAndParam;
import com.reeco.ingestion.domain.Parameter;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.NumericalStatByOrgRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.NumericalTsByOrgRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.ParamsByOrgRepository;
import com.reeco.ingestion.utils.Utils;
import com.reeco.ingestion.utils.annotators.Adapter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Adapter
@Log4j2
public class EventStatPersistenceAdapter implements AggregateEventsPort, NumStatRepository {

    @Autowired
    private ParamsByOrgRepository paramsByOrgRepository;

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

    private final LocalDate defaultDate = LocalDate.of(2000, 1, 1);

    @Override
    public Flux<NumericalStatEvent> aggEventByOrgAndParams(Timestamp startTime, Timestamp endTime) {
        return findAllParamsGroupByOrg()
                .log()
                .flatMap(v -> numericalTsByOrgRepository.finAllEventByOrg(
                            v.getOrganizationId(),
                            v.getParamsIds(),
                            startTime,
                            endTime))
                .log()
                .map(v -> numericEventMapper.toDomain(v))
                .groupBy(v -> new OrgAndParam(v.getOrganizationId(),
                        v.getParamId(),
                        Utils.convertDateTimeToDate(v.getEventTime())))
                .flatMap(
                        group -> group
                                .map(NumericalTsEvent::getValue)
                                .collectList()
                                .map(v -> {
                                    StatsAccumulator statsAccumulator = new StatsAccumulator();
                                    statsAccumulator.addAll(v);
                                    return new NumericalStatEvent(
                                            group.key().getOrganizationId(),
                                            group.key().getParamId(),
                                            group.key().getDate(),
                                            statsAccumulator.min(),
                                            statsAccumulator.max(),
                                            statsAccumulator.mean(),
                                            statsAccumulator.sum(),
                                            statsAccumulator.count(),
                                            statsAccumulator.populationStandardDeviation(),
                                            LocalDateTime.now()
                                    );
                                })
                );
    }

    @Override
    public void insert(LocalDateTime localDateTime) {
        String strStart = "2021-10-11 21:04:23.344";
        String strEnd = "2021-10-13 11:30:00.234";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime endTime = LocalDateTime.parse(strEnd, formatter).atZone(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime startTime = endTime.minusDays(2);
        Timestamp timestampEnd = Timestamp.valueOf(endTime);
        Timestamp timestampStart = Timestamp.valueOf(startTime);
        aggEventByOrgAndParams(timestampStart, timestampEnd)
                .map(v-> numStatEventMapper.toPort(v))
                .flatMap(v->numericalStatByOrgRepository.save(v))
                .subscribe(v->log.info("SAVE: "+v.toString()));

    }


    public Flux<Parameter.ParamsByOrg> findAllParamsGroupByOrg() {
        return paramsByOrgRepository
                .findAll()
                .map(v -> parameterMapper.toDomain(v))
                .groupBy(Parameter::getOrganizationId)
                .flatMap(
                        group -> group
                                .map(Parameter::getParamId)
                                .collectList()
                                .map(list -> {
                                    Map<Long, List<Long>> mapper = new HashMap<>();
                                    mapper.put(group.key(), list);
                                    return new Parameter.ParamsByOrg(group.key(), list);
                                })
                );
    }

}
