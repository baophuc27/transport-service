package com.reeco.transport.infrastructure.persistence.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostgresCustomIdsRepository extends JpaRepository<CustomIdEntity,Integer>{

    @Query(value = "DELETE FROM public.customids cid WHERE cid.id_type = ?1 AND cid.original_id = ?2",nativeQuery = true)
    void deleteCustomIdEntity(String customIdType, Integer originalId);

    @Query(value = "SELECT cid.original_id FROM public.customids cid WHERE cid.id_type = 'CONNECTION' and cid.custom_id = ?1", nativeQuery = true)
    Integer getConnectionIdFromCustomId(String customId);

    @Query(value = "SELECT cid.custom_id FROM public.customids cid WHERE cid.id_type='CONNECTION' and cid.original_id = ?1 ORDER BY cid.id DESC LIMIT 1",nativeQuery = true)
    String getCustomIdFromConnectionId(Integer deviceId);

    @Query(value = "SELECT * FROM public.customids cid WHERE cid.custom_id = ?1 ORDER BY cid.id DESC LIMIT 1",nativeQuery = true)
    CustomIdEntity getEntityFromCustomId(String customId);
}
