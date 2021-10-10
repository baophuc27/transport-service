package com.reeco.ingestion.application.port.out;


import com.reeco.ingestion.domain.Device;
import reactor.core.publisher.Mono;

public interface DeviceRepository{
    void insert(Device deviceConnection);

    Mono<Device> findById(String id);

    void delete(Device deviceConnection);
}
