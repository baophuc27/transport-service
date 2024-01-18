package com.reeco.core.dmp.core.repository.cassandra;

import com.reeco.core.dmp.core.annotations.Traceable;
import com.reeco.core.dmp.core.model.cassandra.CategoricalTsByOrg;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoricalTsByOrgRepository extends CassandraRepository<CategoricalTsByOrg, Long> {

    @Traceable
    @Query("select * from categorical_series_by_organization\n" +
            "where event_time >= ?0 and event_time <= ?1 \n" +
            "  and organization_id = ?2  \n" +
            "  and param_id = ?3 \n" +
            "  ALLOW FILTERING ;")
    List<CategoricalTsByOrg> findDataDetail(Timestamp startTime, Timestamp endTime, Long organizationId, Long paramId);

    @Traceable
    @Query("SELECT * FROM categorical_series_by_organization WHERE organization_id=?0 and param_id=?1\n" +
            "LIMIT 2 ALLOW FILTERING ;")
    List<CategoricalTsByOrg> find2LatestRow(Long orgId, Long paramId);

    @Traceable
    @Query("SELECT * FROM categorical_series_by_organization WHERE organization_id=?0 and param_id=?1\n" +
            "LIMIT 1;")
    Optional<CategoricalTsByOrg> find1LatestRow(Long orgId, Long paramId);
}
