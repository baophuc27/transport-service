package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumericalTsByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.ParamsByOrg;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ParamsByOrgRepository extends ReactiveCassandraRepository<ParamsByOrg, ParamsByOrg.Key> {
    Mono<ParamsByOrg> findById(@NotNull ParamsByOrg.Key key);
    @Override
    <S extends ParamsByOrg> Mono<S> save(S entity);
}
