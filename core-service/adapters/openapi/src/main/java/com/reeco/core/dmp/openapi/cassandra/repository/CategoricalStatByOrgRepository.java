package com.reeco.core.dmp.openapi.cassandra.repository;

import com.reeco.core.dmp.openapi.cassandra.entity.CategoricalStatByOrg;

import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoricalStatByOrgRepository
        extends ReactiveCassandraRepository<CategoricalStatByOrg, CategoricalStatByOrg.Key> {

}
