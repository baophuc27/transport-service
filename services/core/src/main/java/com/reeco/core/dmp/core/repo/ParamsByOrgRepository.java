package com.reeco.core.dmp.core.repo;

import com.reeco.core.dmp.core.model.ParamsByOrg;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface ParamsByOrgRepository extends CassandraRepository<ParamsByOrg, Long> {
//    Optional<ParamsByOrg> findById(@NotNull ParamsByOrg.Key key);

    Optional<ParamsByOrg> findByPartitionKeyOrganizationIdAndPartitionKeyParamId(Long orgId, Long paramId);
}
