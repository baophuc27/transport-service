package com.reeco.ingestion.adapter.out;

import com.reeco.ingestion.application.port.out.IndicatorRepository;
import com.reeco.ingestion.application.port.out.ParameterRepository;
import com.reeco.ingestion.domain.Indicator;
import com.reeco.ingestion.domain.Parameter;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.IndicatorInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

public class IndicatorPersistenceAdapter implements IndicatorRepository {

    @Autowired
    IndicatorInfoRepository indicatorInfoRepository;

    @Override
    public Mono<Indicator> findById(Long indicatorId) {
        return null;
    }
}