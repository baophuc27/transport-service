package com.reeco.http.infrastructure.persistence.cassandra.repository;

import com.reeco.common.model.enumtype.TransportType;
import com.reeco.http.infrastructure.persistence.cassandra.entity.ConnectionByOrg;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionByOrgRepository extends CassandraRepository<ConnectionByOrg, Long> {
    @Query("select * from reecotech.device_connection_by_organization\n" +
            "where transport_type = ?0 ALLOW FILTERING ;")
    List<ConnectionByOrg> getByTransportType(TransportType transportType);

    @Query("select * from reecotech.device_connection_by_organization\n" +
            "where connection_id =  ?0 AND transport_type = ?1 ALLOW FILTERING;")
    Optional<ConnectionByOrg> getByIdAndTransportType(Long conectionId, TransportType transportType);
}
