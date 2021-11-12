package com.reeco.ingestion.adapter.out;

import com.datastax.oss.driver.shaded.guava.common.math.StatsAccumulator;
import com.reeco.ingestion.application.mapper.NumericEventMapper;
import com.reeco.ingestion.application.mapper.ParameterMapper;
import com.reeco.ingestion.application.port.in.IncomingTsEvent;
import com.reeco.ingestion.application.port.in.LoadOrgAndParamPort;
import com.reeco.ingestion.application.port.out.AggregateEventsPort;
import com.reeco.ingestion.application.port.out.NumStatRepository;
import com.reeco.ingestion.application.port.out.ParameterRepository;
import com.reeco.ingestion.domain.*;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.CategoricalStatByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumericalStatByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.NumericalStatByOrgRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.NumericalTsByOrgRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.ParamsByOrgRepository;
import com.reeco.ingestion.utils.Utils;
import com.reeco.ingestion.utils.annotators.Adapter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.DoubleStream;

@Adapter
@Log4j2
public class EventStatPersistenceAdapter implements AggregateEventsPort, NumStatRepository {

    @Autowired
    private ParamsByOrgRepository paramsByOrgRepository;

    @Autowired
    NumericalTsByOrgRepository numericalTsByOrgRepository;

    @Autowired
    NumericalStatByOrgRepository numericalStatByOrgRepository;

    @Autowired
    private NumericEventMapper numericEventMapper;

    @Autowired
    private ParameterMapper parameterMapper;

    private final LocalDate defaultDate = LocalDate.of(1970, 1, 1);

    @Override
    public Flux<NumStatisticEvent> aggEventByOrgAndParams() {
        return findAllParamsGroupByOrg()
                .log()
                .flatMap(v -> numericalTsByOrgRepository.finAllEventByOrg(v.getOrganizationId(), v.getParamsId()))
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
                                    return new NumStatisticEvent(
                                            group.key().getOrganizationId(),
                                            group.key().getParamId(),
                                            group.key().getDate(),
                                            statsAccumulator.min(),
                                            statsAccumulator.max(),
                                            statsAccumulator.mean(),
                                            statsAccumulator.sum(),
                                            statsAccumulator.count(),
                                            LocalDateTime.now()
                                    );
                                })
                );
    }


    @Override
    public void insert(LocalDateTime localDateTime) {
        String strStart = "2021-10-10 21:03:23.344+0000";
        String strEnd = "2021-11-25 11:30:00.234+0000";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSX");
        LocalDateTime startDate = LocalDateTime.parse(strEnd, formatter);
        LocalDateTime endDate = LocalDateTime.parse(strStart, formatter);
        getMaxCycleStatistic(10L).subscribe(log::info);
        aggEventByOrgAndParams().subscribe(log::info);

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

    public Mono<LocalDate> getMaxCycleStatistic(Long organizationId) {
        return numericalStatByOrgRepository
                .findMaxStatisticDateByOrg(organizationId)
                .map(v -> v.getPartitionKey().getDate())
                .switchIfEmpty(Mono.just(defaultDate));
    }
}
