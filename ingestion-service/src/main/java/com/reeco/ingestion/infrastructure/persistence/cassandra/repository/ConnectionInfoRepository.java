package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.ConnectionInfo;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface ConnectionInfoRepository extends ReactiveCassandraRepository<ConnectionInfo, ConnectionInfo.Key> {
}
