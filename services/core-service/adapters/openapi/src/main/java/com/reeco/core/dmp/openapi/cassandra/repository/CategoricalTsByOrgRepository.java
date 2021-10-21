package com.reeco.core.dmp.openapi.cassandra.repository;

import java.util.List;

import com.reeco.core.dmp.openapi.cassandra.entity.CategoricalTsByOrg;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface CategoricalTsByOrgRepository
        extends ReactiveCassandraRepository<CategoricalTsByOrg, CategoricalTsByOrg.Key> {

    List<CategoricalTsByOrg> findByPartitionKeyOrganizationIdAndPartitionKeyParamId(Long orgId, Long paramId);

    @Query("SELECT * FROM categorical_series_by_organization;")
    Flux<CategoricalTsByOrg> findAll();

}
