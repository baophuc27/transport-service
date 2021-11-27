package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.AlarmInfo;
import org.reactivestreams.Publisher;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlarmInfoRepository extends ReactiveCassandraRepository<AlarmInfo, AlarmInfo.Key> {

    @Query("SELECT * FROM alarm WHERE organization_id = ?0 AND param_id = ?1")
    Flux<AlarmInfo> findByOrgAndParam(Long orgId, Long paramId);

    @Query("DELETE FROM alarm WHERE organization_id = ?0 AND para_id = ?1")
    Mono<Void> deleteByOrgAndParam(Long orgId, Long paramId);
}
