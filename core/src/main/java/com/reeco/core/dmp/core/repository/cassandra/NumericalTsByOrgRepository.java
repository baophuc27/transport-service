package com.reeco.core.dmp.core.repository.cassandra;


import com.reeco.core.dmp.core.annotations.Traceable;
import com.reeco.core.dmp.core.model.cassandra.NumericalTsByOrg;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface NumericalTsByOrgRepository extends CassandraRepository<NumericalTsByOrg, NumericalTsByOrg.Key> {

    @Traceable
    List<NumericalTsByOrg> findByPartitionKeyOrganizationIdAndPartitionKeyParamId(Long orgId, Long pramId);

    @Traceable
    @Query("select * from numeric_series_by_organization\n" +
            "where event_time >= ?0 and event_time <= ?1 \n" +
            "  and organization_id = ?2  \n" +
            "  and param_id = ?3;")
    List<NumericalTsByOrg> findDataDetail(Timestamp startTime, Timestamp endTime, Long organizationId, Long paramId);

    @Traceable
    @Query("select * from numeric_series_by_organization\n" +
            "where event_time >= ?0 and event_time <= ?1 \n" +
            "  and organization_id = ?2  \n" +
            "  and param_id in ?3;")
    List<NumericalTsByOrg> findDataByParams(Timestamp startTime, Timestamp endTime, Long organizationId, List<Long> paramId);

    @Traceable
    @Query("SELECT * FROM numeric_series_by_organization WHERE organization_id=?0 and param_id=?1\n" +
            "LIMIT 2;")
    List<NumericalTsByOrg> find2LatestRow(Long orgId, Long paramId);

    @Traceable
    @Query("SELECT * FROM numeric_series_by_organization WHERE organization_id=?0 and param_id=?1\n" +
            "LIMIT 1;")
    Optional<NumericalTsByOrg> find1LatestRow(Long orgId, Long paramId);

    @Traceable
    Optional<NumericalTsByOrg> findByPartitionKey(NumericalTsByOrg.Key key);
}