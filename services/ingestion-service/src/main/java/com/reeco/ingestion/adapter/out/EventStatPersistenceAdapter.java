package com.reeco.ingestion.adapter.out;

import com.reeco.ingestion.application.mapper.NumericEventMapper;
import com.reeco.ingestion.application.mapper.ParameterMapper;
import com.reeco.ingestion.application.port.out.AggregateEventsPort;
import com.reeco.ingestion.application.port.out.NumStatRepository;
import com.reeco.ingestion.application.port.out.ParameterRepository;
import com.reeco.ingestion.domain.CategoricalTsEvent;
import com.reeco.ingestion.domain.NumericalTsEvent;
import com.reeco.ingestion.domain.Parameter;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.CategoricalStatByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.NumericalStatByOrgRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.NumericalTsByOrgRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.ParamsByOrgRepository;
import com.reeco.ingestion.utils.annotators.Adapter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Adapter
@Log4j2
public class EventStatPersistenceAdapter implements AggregateEventsPort {

    @Autowired
    private ParamsByOrgRepository paramsByOrgRepository;

    @Autowired
    NumericalTsByOrgRepository numericalTsByOrgRepository;

    @Autowired
    private NumericEventMapper numericEventMapper;

    @Autowired
    private ParameterMapper parameterMapper;


    public Mono<NumericalTsEvent> insertNumStatEvent(NumericalTsEvent event) {
        return null;
    }

    public Mono<CategoricalTsEvent> insertCatStatEvent(CategoricalTsEvent event) {
        return null;
    }

    @Override
    public Mono<NumericalTsEvent> aggEvents(Long orgId, List<Long> paramIds) {
        return null;
    }


    @Override
    public void insert() {
        String strStart = "2021-10-10 11:30:00";
        String strEnd = "2021-10-25 11:30:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate =  LocalDateTime.parse(strEnd, formatter);
        LocalDateTime endDate =  LocalDateTime.parse(strStart, formatter);
        findAllParamsGroupByOrg()
                .log()
                .flatMap(v -> numericalTsByOrgRepository.finAllEventByOrg(v.getOrganizationId(), v.getParamsId()))
                .log()
                .subscribe(log::info);
    }

    @Override
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
