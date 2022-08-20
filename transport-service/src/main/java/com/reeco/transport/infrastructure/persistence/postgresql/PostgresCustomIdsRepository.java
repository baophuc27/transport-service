package com.reeco.transport.infrastructure.persistence.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostgresCustomIdsRepository extends JpaRepository<CustomIdEntity,Integer>{

    @Query(value = "DELETE FROM public.customids cid WHERE cid.id_type = ?1 AND cid.original_id = ?2",nativeQuery = true)
    void deleteCustomIdEntity(String customIdType, Integer originalId);

}
