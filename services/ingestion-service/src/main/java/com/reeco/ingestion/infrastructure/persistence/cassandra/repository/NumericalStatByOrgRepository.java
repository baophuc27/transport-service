package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumericalStatByOrg;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface NumericalStatByOrgRepository extends ReactiveCassandraRepository<NumericalStatByOrg, NumericalStatByOrg.Key> {
    @NotNull
    @Override
    <S extends NumericalStatByOrg> Flux<S> saveAll(Publisher<S> entityStream);

    @Override
    <S extends NumericalStatByOrg> Mono<S> save(S entity);
}
