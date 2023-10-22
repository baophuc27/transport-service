package com.reeco.transport.infrastructure.persistence.postgresql;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


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

    @Query(value = "SELECT device.id FROM public.device device WHERE device.active = true",nativeQuery = true)
    List<Integer> getRegisteredDevices();

    @Query(value = "select * FROM public.device device WHERE device.logged_out = false",nativeQuery = true)
    List<DeviceEntity> getConnectedDevices();

    @Transactional
    @Modifying
    @Query(value = "UPDATE public.device set logged_out = ?2, last_active = ?3 WHERE id = ?1",nativeQuery = true)
    void updateDeviceLoggedOut(Integer deviceId, boolean logged_out, LocalDateTime now);
}
