package com.reeco.ingestion.adapter.out;

import com.reeco.ingestion.application.mapper.CategoricalTsEventMapper;
import com.reeco.ingestion.application.mapper.NumericalTsEventMapper;
import com.reeco.ingestion.application.port.out.ParameterRepository;
import com.reeco.ingestion.application.port.out.TsEventRepository;
import com.reeco.ingestion.domain.Parameter;
import com.reeco.ingestion.domain.NumericalTsEvent;
import com.reeco.ingestion.domain.CategoricalTsEvent;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.*;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.CategoricalTsByOrgRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.IndicatorInfoRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.NumericalTsByOrgRepository;
import com.reeco.ingestion.utils.annotators.Adapter;
import com.reeco.ingestion.utils.exception.EventProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.ReactiveCassandraBatchOperations;
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate;
import reactor.core.publisher.Mono;

@Adapter
@Log4j2
public class TsEventPersistenceAdapter implements TsEventRepository {

    @Autowired
    private IndicatorInfoRepository indicatorRepository;

    @Autowired
    private NumericalTsByOrgRepository numericalTsByOrgRepository;

    @Autowired
    private CategoricalTsByOrgRepository categoricalTsByOrgRepository;

    @Autowired
    private NumericalTsEventMapper numericalTsEventMapper;

    @Autowired
    private CategoricalTsEventMapper categoricalTsEventMapper;

    @Override
    public void insertNumericEvent(NumericalTsEvent event) {
        numericalTsByOrgRepository
                .save(numericalTsEventMapper.toPort(event))
                .log()
                .subscribe();
    }

    @Override
    public void insertCategoricalEvent(CategoricalTsEvent event) {
        categoricalTsByOrgRepository
                .save(categoricalTsEventMapper.toPort(event))
                .log()
                .subscribe();
    }

}
