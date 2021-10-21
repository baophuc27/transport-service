package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumericalTsByOrg;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface NumericalTsByOrgRepository extends ReactiveCassandraRepository<NumericalTsByOrg, NumericalTsByOrg.Key> {
    @Override
    <S extends NumericalTsByOrg> Mono<S> save(S entity);
}
