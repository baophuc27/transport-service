package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.CategoricalStatByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.CategoricalTsByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumericalTsByOrg;
import org.reactivestreams.Publisher;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CategoricalTsByOrgRepository extends ReactiveCassandraRepository<CategoricalTsByOrg, CategoricalTsByOrg.Key> {

    @Query(value = "SELECT * FROM categorical_series_by_organization WHERE organization_id = ?0 AND param_id = ?1 AND event_time >= ?2 AND event_time <= ?3")
    Flux<CategoricalTsByOrg> findAllEventByOrg(Long organizationId,
                                             Long paramId,
                                             Timestamp startTime,
                                             Timestamp endTime);

}
