package com.reeco.core.dmp.core.repo;


import com.reeco.core.dmp.core.model.NumericalTsByOrg;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NumericalTsByOrgRepository extends CassandraRepository<NumericalTsByOrg, NumericalTsByOrg.Key> {

    List<NumericalTsByOrg> findByPartitionKeyOrganizationIdAndPartitionKeyParamId(Long orgId, Long pramId);

    @Query("select * from numeric_series_by_organization\n" +
            "where event_time >= ?0 and event_time <= ?1 \n" +
            "  and organization_id = ?2  \n" +
            "  and param_id = ?3 \n" +
            "  ALLOW FILTERING ;")
    List<NumericalTsByOrg> findDataDetail(Timestamp startTime, Timestamp endTime, Long organizationId, Long paramId);

    @Query("SELECT * FROM numeric_series_by_organization WHERE organization_id=?0 and param_id=?1\n" +
            "LIMIT 2 ALLOW FILTERING ;")
    List<NumericalTsByOrg> find2LatestRow(Long orgId, Long paramId);

    Optional<NumericalTsByOrg> findByPartitionKey(NumericalTsByOrg.Key key);
}