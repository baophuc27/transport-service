package com.reeco.transport.infrastructure.persistence.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface PostgresMQTTConfigRepository extends JpaRepository<MQTTConfigEntity, String> {
    @Transactional
    @Modifying
    @Query(value="DELETE FROM public.mqtt_acls mqtt WHERE id=?1",nativeQuery = true)
    void deleteMQTTConfigEntityById(String id);
}
