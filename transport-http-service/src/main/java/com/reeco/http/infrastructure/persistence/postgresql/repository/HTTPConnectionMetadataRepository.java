package com.reeco.http.infrastructure.persistence.postgresql.repository;

import com.reeco.http.infrastructure.persistence.postgresql.entity.HTTPConnectionMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HTTPConnectionMetadataRepository extends JpaRepository<HTTPConnectionMetadata, Long> {

}
