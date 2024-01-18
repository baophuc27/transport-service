package com.reeco.core.dmp.core.repository.cassandra;

import com.reeco.core.dmp.core.annotations.Traceable;
import com.reeco.core.dmp.core.model.cassandra.ParamsByOrg;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParamsByOrgRepository extends CassandraRepository<ParamsByOrg, Long> {
//    Optional<ParamsByOrg> findById(@NotNull ParamsByOrg.Key key);

    @Traceable
    Optional<ParamsByOrg> findByPartitionKeyOrganizationIdAndPartitionKeyParamId(Long orgId, Long paramId);

    @Traceable
    @Query("select * from reecotech.params_by_organization\n" +
            "where organization_id = ?0 and connection_id = ?1 ALLOW FILTERING ;")
    List<ParamsByOrg> findParamByConnection(Long orgId, Long connectionId);
}
