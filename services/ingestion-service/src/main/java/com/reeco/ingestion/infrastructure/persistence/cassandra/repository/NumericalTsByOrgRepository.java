package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumericalTsByOrg;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NumericalTsByOrgRepository extends ReactiveCassandraRepository<NumericalTsByOrg, NumericalTsByOrg.NumericalTsKey> {
    @Override
    <S extends NumericalTsByOrg> Mono<S> save(S entity);

    @Query("SELECT organization_id as organizationId, param_id as paramId, MIN(value) as min, MAX(value) as max, AVG(value) as mean, SUM(value) as acc, COUNT(value) as count" +
            " FROM numeric_series_by_organization WHERE organization_id = ?0 and param_id IN ?1 and event_time >= ?2 GROUP BY organization_id, param_id")
    Flux<StatEvent> findNumStatEventAllOrgAndParamsInDay(Long organizationId, List<Long> paramIds, LocalDateTime startTime);

    @Query(value = "SELECT * FROM numeric_series_by_organization WHERE organization_id = ?0 AND param_id IN ?1 AND event_time >= ?2 AND event_time <= ?3")
    Flux<NumericalTsByOrg> finAllEventByOrg(Long organizationId,
                                            List<Long> paramIds,
                                            Timestamp startTime,
                                            Timestamp endTime);

    @Query(value = "SELECT organization_id as organizationId FROM numeric_series_by_organization WHERE organization_id = ?0 AND param_id IN ?1 ")
    Flux<Result> getAvgValueByOrgAndParams(Long organizationId, List<Long> paramIds);

    public interface StatEvent {
        Long getOrganizationId();
        Long getParamId();
        Double getMin();
        Double getMax();
        Double getMean();
        Double getAcc();
        Double getCount();
    }

    @Data
    @AllArgsConstructor
    public class Result implements Serializable {
        Long organizationId;
    }

}
