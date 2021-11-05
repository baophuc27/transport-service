package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.IndicatorInfo;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IndicatorInfoRepository extends ReactiveCassandraRepository<IndicatorInfo, IndicatorInfo.Key> {
    Mono<IndicatorInfo> findById(IndicatorInfo.Key key);
}
