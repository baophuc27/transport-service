package com.reeco.core.dmp.core.repository.cassandra;

import com.reeco.core.dmp.core.annotations.Traceable;
import com.reeco.core.dmp.core.model.cassandra.Alarm;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;

public interface AlarmRepository extends CassandraRepository<Alarm, Alarm.Key> {

    @Traceable
    List<Alarm> findByPartitionKeyOrganizationIdAndPartitionKeyParamId(Long orgId, Long paramId);
}
