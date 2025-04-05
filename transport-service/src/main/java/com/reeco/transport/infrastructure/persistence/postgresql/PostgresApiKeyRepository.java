package com.reeco.transport.infrastructure.persistence.postgresql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface PostgresApiKeyRepository extends JpaRepository<ApiKeyEntity,Integer> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM public.api_key api WHERE id = ?1",nativeQuery = true)
    void deleteApiKeyEntityById(Integer id);

    @Query(value = "SELECT * FROM public.api_key WHERE KEY = ?1 ORDER BY id DESC LIMIT 1",nativeQuery = true)
    ApiKeyEntity getApiKeyEntitybyKey(String api_key);
}
