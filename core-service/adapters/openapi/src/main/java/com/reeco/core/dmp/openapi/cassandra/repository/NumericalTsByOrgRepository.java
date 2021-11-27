package com.reeco.core.dmp.openapi.cassandra.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.reeco.core.dmp.openapi.cassandra.entity.NumericalTsByOrg;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@NoRepositoryBean
public interface NumericalTsByOrgRepository
        extends ReactiveCassandraRepository<NumericalTsByOrg, NumericalTsByOrg.Key> {
    @Override
    <S extends NumericalTsByOrg> Mono<S> save(S entity);
      
    Flux<NumericalTsByOrg> findByPartitionKeyOrganizationIdAndPartitionKeyParamId(Long orgId, Long pramId);
    // @Query("select * from numeric_series_by_organization +\n"
    //         "where organization_id = 1 and param_id = 2 and event_time <= '2021-10-01T14:00:00.00' + \n"
    //         "and event_time >= '2021-10-01T13:45:00.00' +\n"
    //         "ALLOW FILTERING;")
    // List<NumericalTsByOrgRepository> findByRange(Long orgId, Long paramId, LocalDateTime start, LocalDateTime end);
}
