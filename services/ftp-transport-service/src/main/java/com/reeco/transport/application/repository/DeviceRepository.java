package com.reeco.transport.application.repository;


import com.reeco.transport.domain.DeviceConnection;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface DeviceRepository{
    void insert(DeviceConnection deviceConnection);

    Mono<DeviceConnection> findById(String id);

    void delete(DeviceConnection deviceConnection);
}
