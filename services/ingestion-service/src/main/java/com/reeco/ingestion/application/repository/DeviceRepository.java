package com.reeco.ingestion.application.repository;


import com.reeco.ingestion.domain.DeviceConnection;
import reactor.core.publisher.Mono;

public interface DeviceRepository{
    void insert(DeviceConnection deviceConnection);

    Mono<DeviceConnection> findById(String id);

    void delete(DeviceConnection deviceConnection);
}
