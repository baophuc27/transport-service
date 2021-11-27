package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.AlarmInfo;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface AlarmInfoRepository extends ReactiveCassandraRepository<AlarmInfo, AlarmInfo.Key> {
}
