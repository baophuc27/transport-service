package com.reeco.transport.infrastructure.persistence.postgresql;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;


@Repository
public interface PostgresDeviceRepository extends JpaRepository<DeviceEntity, Integer> {

    @Query(value = "SELECT * FROM public.device device WHERE device.id = ?1",nativeQuery = true)
    Collection<DeviceEntity> findDeviceById(Integer deviceId);

    @Query(value = "SELECT device.template_format FROM public.device device WHERE device.id = ?1",nativeQuery = true)
    Integer findTemplateById(Integer deviceId);
}
