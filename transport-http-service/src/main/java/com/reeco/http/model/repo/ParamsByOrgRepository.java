package com.reeco.http.model.Repo;

import com.reeco.http.model.Entity.ParamsByOrg;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParamsByOrgRepository extends CassandraRepository<ParamsByOrg, Long> {
//    Optional<ParamsByOrg> findById(@NotNull ParamsByOrg.Key key);

    Optional<ParamsByOrg> findByPartitionKeyOrganizationIdAndPartitionKeyParamId(Long orgId, Long paramId);


    @Query("select * from reecotech.params_by_organization\n" +
            "where organization_id = ?0 and connection_id = ?1 ALLOW FILTERING ;")
    List<ParamsByOrg> findParamByConnection(Long orgId, Long connectionId);
}
