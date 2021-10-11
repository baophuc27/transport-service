package com.reeco.ingestion.adapter.out;

import com.reeco.ingestion.application.port.out.DeviceRepository;
import com.reeco.ingestion.domain.Device;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.DeviceByStation;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.DeviceByStationRepository;
import reactor.core.publisher.Mono;

public class DevicePersistenceAdapter implements DeviceRepository {

    DeviceByStationRepository deviceByStationRepository;

    @Override
    public void insert(Device deviceConnection) {

    }

    @Override
    public Mono<Device> findById(String id) {
        return null;
    }

    @Override
    public void delete(Device deviceConnection) {

    }
}
