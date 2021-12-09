package com.reeco.http.model.repo;

import com.reeco.common.model.enumtype.TransportType;
import com.reeco.http.model.entity.ConnectionByOrg;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;

public interface ConnectionByOrgRepository extends CassandraRepository<ConnectionByOrg, Long> {
    @Query("select * from reecotech.device_connection_by_organization\n" +
            "where transport_type = ?0 ALLOW FILTERING ;")
    List<ConnectionByOrg> getByTransportType(TransportType transportType);
}
