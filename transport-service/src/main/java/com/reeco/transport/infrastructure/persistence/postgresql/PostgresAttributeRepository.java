package com.reeco.transport.infrastructure.persistence.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PostgresAttributeRepository extends JpaRepository<AttributeEntity,Integer> {

    @Query(value = "SELECT * FROM public.attribute attr WHERE attr.device_id = ?1", nativeQuery = true)
    Collection<AttributeEntity> findRegisteredAttributes(Integer deviceId);

    @Query(value = "SELECT * FROM public.attribute attr WHERE attr.device_id = ?1 AND attr.key_name = ?2 ORDER BY attr.id DESC LIMIT 1", nativeQuery = true)
    AttributeEntity findMappingAttribute(Integer deviceId, String key);

//    @Query(value = "SELECT * FROM public.attribute attr WHERE attr.device_id = ?1 AND attr.source_param_name = ?2 ORDER BY attr.id DESC LIMIT 1",nativeQuery = true)
//    AttributeEntity findMappingAttribute(Integer deviceId, String key);

    @Query(value = "DELETE FROM public.attribute attr WHERE attr.device_id = ?1 AND attr.attribute_id = ?2", nativeQuery = true)
    void deleteAttribute(Integer deviceId, Integer attributeId);

    @Query(value = "SELECT * FROM public.attribute attr WHERE attr.device_id = ?1 AND attr.attribute_id =?2 LIMIT 1", nativeQuery = true)
    AttributeEntity findAttribute(Integer deviceId, Integer attributeId);

    @Query(value = "SELECT * FROM public.attribute attr WHERE attr.id = ?1", nativeQuery = true)
    AttributeEntity findParameterById(Integer parameterId);

    @Query(value = "SELECT * FROM public.attribute attr WHERE attr.organization_id = ?1", nativeQuery = true)
    Collection<AttributeEntity> findListParameterByOrganizationId(Integer id);

    @Query(value = "SELECT * FROM public.attribute attr WHERE attr.station_id = ?1", nativeQuery = true)
    Collection<AttributeEntity> findListParameterByStationId(Integer id);

    @Query(value = "SELECT * FROM public.attribute attr WHERE attr.device_id = ?1", nativeQuery = true)
    Collection<AttributeEntity> findListParameterByConnectionId(Integer id);
}
