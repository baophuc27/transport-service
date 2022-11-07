package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.ConnectionAlarmInfo;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.ConnectionInfo;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ConnectionAlarmInfoRepository extends ReactiveCassandraRepository<ConnectionAlarmInfo, ConnectionAlarmInfo.ConnectionAlarmKey> {

    @Override
    <S extends ConnectionAlarmInfo> Mono<S> save(S entity);
}
