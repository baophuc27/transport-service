package com.reeco.ingestion.adapter.out;

import com.reeco.ingestion.application.port.out.IndicatorRepository;
import com.reeco.ingestion.domain.Indicator;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.IndicatorInfo;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.IndicatorInfoRepository;
import com.reeco.ingestion.utils.annotators.Adapter;
import com.reeco.ingestion.application.mapper.IndicatorMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

@Adapter
@Log4j2
public class IndicatorPersistenceAdapter implements IndicatorRepository {

    @Autowired
    private IndicatorInfoRepository indicatorInfoRepository;

    @Autowired
    private IndicatorMapper indicatorMapper;

    @Override
    public Mono<Indicator> findById(Long indicatorId) {
        return indicatorInfoRepository
                .findById(new IndicatorInfo.Key(indicatorId))
                .map(v -> indicatorMapper.toDomain(v));
    }

    @Override
    public Mono<Indicator> save(Indicator indicator) {
        return null;
    }

}