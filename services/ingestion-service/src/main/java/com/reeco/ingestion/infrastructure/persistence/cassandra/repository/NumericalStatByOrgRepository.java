package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumericalStatByOrg;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NumericalStatByOrgRepository extends ReactiveCassandraRepository<NumericalStatByOrg, NumericalStatByOrg.NumericalStatKey> {
    @Query("SELECT organization_id, param_id as paramId, MIN(value) as min, MAX(value) as max, AVG(value) as mean, SUM(value) as acc, COUNT(value) as count" +
            " FROM numeric_series_by_organization WHERE organization_id = ?0 and param_id IN ?1 and event_time >= ?2 GROUP BY organization_id, param_id")
    Flux<NumericalStatByOrg> findNumStatEventAllOrgAndParamsInDay(Long organizationId, List<Long> paramIds, LocalDateTime startTime);

    @Query("SELECT * FROM numeric_stats_by_organization WHERE organization_id = ?0 LIMIT 1")
    Mono<NumericalStatByOrg> findMaxStatisticDateByOrg(Long organizationId);

    @Query("SELECT * FROM numeric_stats_by_organization WHERE organization_id = ?0")
    Flux<NumericalStatByOrg> findAllByOrgId(Long organizationId);
}
