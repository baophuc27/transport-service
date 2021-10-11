package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumTsByStation;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface TsByStationRepository extends ReactiveCassandraRepository<NumTsByStation, NumTsByStation.Key> {
}
