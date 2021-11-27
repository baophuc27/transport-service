package com.reeco.transport.application.repository;


import com.reeco.transport.domain.DeviceConnection;
import reactor.core.publisher.Mono;

public interface DeviceRepository{
    void insert(DeviceConnection deviceConnection);

    Mono<DeviceConnection> findById(String id);

    void delete(DeviceConnection deviceConnection);
}
