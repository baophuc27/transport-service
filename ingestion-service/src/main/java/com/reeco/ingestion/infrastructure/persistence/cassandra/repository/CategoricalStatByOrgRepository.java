package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.CategoricalStatByOrg;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoricalStatByOrgRepository extends ReactiveCassandraRepository<CategoricalStatByOrg, CategoricalStatByOrg.Key> {

}
