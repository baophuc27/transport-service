package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.DeviceByStation;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;

import reactor.core.publisher.Flux;

public interface DeviceByStationRepository extends ReactiveCassandraRepository<DeviceByStation, DeviceByStation.Key> {
    Flux<DeviceByStation> findByPartitionKeyStationId(final Long stationId);
}
