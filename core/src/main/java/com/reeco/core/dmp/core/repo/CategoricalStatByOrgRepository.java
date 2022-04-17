package com.reeco.core.dmp.core.repo;
import com.reeco.core.dmp.core.model.CategoricalStatByOrg;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CategoricalStatByOrgRepository extends CassandraRepository<CategoricalStatByOrg, CategoricalStatByOrg.Key> {

    @Query("select * from categorical_stats_by_organization\n" +
            "where organization_id=?0\n" +
            "and param_id = ?1\n" +
            "and date>= ?2\n" +
            "and date<= ?3\n" +
            "ALLOW FILTERING")
    List<CategoricalStatByOrg> findCategoricalDataDate(Long orgId, Long paramId, LocalDate startTime, LocalDate endTime);



}

