package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumTsByMetric;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface TsByMetricRepository extends ReactiveCassandraRepository<NumTsByMetric, NumTsByMetric.Key> {
}
