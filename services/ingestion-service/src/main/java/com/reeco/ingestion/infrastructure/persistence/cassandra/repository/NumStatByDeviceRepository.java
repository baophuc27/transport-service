package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.MetricByStation;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumStatByDevice;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NumStatByDeviceRepository extends ReactiveCassandraRepository<NumStatByDevice, NumStatByDevice.Key> {
    @NotNull
    @Override
    <S extends NumStatByDevice> Flux<S> saveAll(Publisher<S> entityStream);

    @Override
    <S extends NumStatByDevice> Mono<S> save(S entity);
}
