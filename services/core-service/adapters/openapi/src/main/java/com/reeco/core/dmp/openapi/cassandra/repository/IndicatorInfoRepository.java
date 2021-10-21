package com.reeco.core.dmp.openapi.cassandra.repository;

import com.reeco.core.dmp.openapi.cassandra.entity.Indicator;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IndicatorInfoRepository extends ReactiveCassandraRepository<Indicator, Indicator.Key> {
    Mono<Indicator> findById(Indicator.Key key);
}
