package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.MetricByStation;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MetricByStationRepository extends ReactiveCassandraRepository<MetricByStation, MetricByStation.Key> {
    Mono<MetricByStation> findById(MetricByStation.Key key);
}
