package com.reeco.transport.infrastructure.persistence.postgresql;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;


@Repository
public interface PostgresDeviceRepository extends JpaRepository<DeviceEntity, Integer> {

    @Query(value = "SELECT * FROM public.device device WHERE device.id = ?1",nativeQuery = true)
    DeviceEntity findDeviceById(Integer deviceId);

    @Query(value = "SELECT device.template_format FROM public.device device WHERE device.id = ?1",nativeQuery = true)
    Integer findTemplateById(Integer deviceId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE DeviceEntity d set d.id = ?1 WHERE d.id = ?1")
    void updateDeviceActive(Integer deviceId);
}
