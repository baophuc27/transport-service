package com.reeco.ingestion.adapter.out;

import com.reeco.ingestion.domain.CategoricalTsEvent;
import com.reeco.ingestion.domain.NumericalTsEvent;
import com.reeco.ingestion.application.mapper.CatEventMapper;
import com.reeco.ingestion.application.mapper.NumericEventMapper;
import com.reeco.ingestion.application.port.out.InsertEventPort;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.CategoricalTsByOrgRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.IndicatorInfoRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.NumericalTsByOrgRepository;
import com.reeco.ingestion.utils.annotators.Adapter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

@Adapter
@Log4j2
public class TsEventPersistenceAdapter implements InsertEventPort {

    @Autowired
    private IndicatorInfoRepository indicatorRepository;

    @Autowired
    private NumericalTsByOrgRepository numericalTsByOrgRepository;

    @Autowired
    private CategoricalTsByOrgRepository categoricalTsByOrgRepository;

    @Autowired
    private NumericEventMapper numericEventMapper;

    @Autowired
    private CatEventMapper catEventMapper;

    @Override
    public Mono<NumericalTsEvent> insertNumericEvent(NumericalTsEvent event) {
        return numericalTsByOrgRepository
                .save(numericEventMapper.toPort(event))
                .map(v ->numericEventMapper.toDomain(v));
    }

    @Override
    public Mono<CategoricalTsEvent> insertCategoricalEvent(CategoricalTsEvent event) {
        return categoricalTsByOrgRepository
                .save(catEventMapper.toPersistence(event))
                .map(v -> catEventMapper.toDomain(v));
    }

}
