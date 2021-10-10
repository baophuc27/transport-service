package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumStatByDevice;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.TextStatByDevice;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

public interface TextStatByDeviceRepository extends ReactiveCassandraRepository<TextStatByDevice, TextStatByDevice.Key> {

}
