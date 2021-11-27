package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.CategoricalTsByOrg;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoricalTsByOrgRepository extends ReactiveCassandraRepository<CategoricalTsByOrg, CategoricalTsByOrg.Key> {
}
