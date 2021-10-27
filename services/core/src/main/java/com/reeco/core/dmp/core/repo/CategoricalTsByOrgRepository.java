package com.reeco.core.dmp.core.repo;

import com.reeco.core.dmp.core.model.CategoricalTsByOrg;
import com.reeco.core.dmp.core.model.NumericalTsByOrg;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CategoricalTsByOrgRepository extends CassandraRepository<CategoricalTsByOrg, CategoricalTsByOrg.Key> {

    @Query("select * from categorical_series_by_organization\n" +
            "where event_time >= ?0 and event_time <= ?1 \n" +
            "  and organization_id = ?2  \n" +
            "  and param_id = ?3 \n" +
            "  ALLOW FILTERING ;")
    List<CategoricalTsByOrg> findDataDetail(Timestamp startTime, Timestamp endTime, Long organizationId, Long paramId);
}
