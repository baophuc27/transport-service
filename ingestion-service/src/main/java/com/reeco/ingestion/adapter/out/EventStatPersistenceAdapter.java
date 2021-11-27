package com.reeco.ingestion.adapter.out;

import com.datastax.oss.driver.shaded.guava.common.math.StatsAccumulator;
import com.reeco.ingestion.application.mapper.NumStatEventMapper;
import com.reeco.ingestion.application.mapper.NumericEventMapper;
import com.reeco.ingestion.application.mapper.ParameterMapper;
import com.reeco.ingestion.application.port.out.NumStatRepository;
import com.reeco.ingestion.domain.NumericalStatEvent;
import com.reeco.ingestion.domain.OrgAndParam;
import com.reeco.ingestion.domain.Parameter;
import com.reeco.ingestion.domain.ParamsByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumericalTsByOrg;
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
        return findAllParams()
                .log()
                .flatMap(v -> numericalTsByOrgRepository.finAllEventByOrg(
                            v.getOrganizationId(),
                            v.getParamId(),
                            startTime,
                            endTime)
                        .groupBy(e->new OrgAndParam(e.getPartitionKey().getOrganizationId(),
                                e.getPartitionKey().getParamId(), e.getDate()))
                        .flatMap(gr -> gr
                                .map(NumericalTsByOrg::getValue)
                                .buffer()
                                .map(k-> {
                                        Collections.sort(k);
                                        log.info(gr.key() + " - " + k.size());
                                        StatsAccumulator statsAccumulator = new StatsAccumulator();
                                        statsAccumulator.addAll(k);
                                        return new NumericalStatEvent(
                                                gr.key().getOrganizationId(),
                                                gr.key().getParamId(),
                                                gr.key().getDate(),
                                                statsAccumulator.min(),
                                                statsAccumulator.max(),
                                                statsAccumulator.mean(),
                                                statsAccumulator.sum(),
                                                median(k),
                                                statsAccumulator.count(),
                                                statsAccumulator.populationStandardDeviation(),
                                                LocalDateTime.now()
                                        );
                                    })
                                )
                );
    }

    private Double median(List<Double> sortedArr){
        int middle = sortedArr.size() / 2;
        middle = middle > 0 && middle % 2 == 0 ? middle - 1 : middle;
        return sortedArr.get(middle);
    }

    @Override
    public void insert(Timestamp startTime, Timestamp endTime) {
        aggEventByOrgAndParams(startTime, endTime)
                .map(v-> numStatEventMapper.toPersistence(v))
                .flatMap(v->numericalStatByOrgRepository.save(v))
                .subscribe(v->log.info("SAVED: "+v.toString()));

    }


    public Flux<ParamsByOrg> findAllParamsGroupByOrg() {
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
                                    return new ParamsByOrg(group.key(), list);
                                })
                );
    }

    public Flux<Parameter> findAllParams() {
        return paramsByOrgRepository
                .findAll()
                .map(v -> parameterMapper.toDomain(v));
    }
}
