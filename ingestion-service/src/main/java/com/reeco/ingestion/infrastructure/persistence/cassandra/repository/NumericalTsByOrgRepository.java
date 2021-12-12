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

    @Query(value = "SELECT * FROM numeric_series_by_organization WHERE organization_id = ?0 AND param_id = ?1 AND event_time >= ?2 AND event_time <= ?3")
    Flux<NumericalTsByOrg> findAllEventByOrg(Long organizationId,
                                            Long paramId,
                                            Timestamp startTime,
                                            Timestamp endTime);

}
