package com.reeco.core.dmp.core.repo;

import com.reeco.core.dmp.core.model.Alarm;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;

public interface AlarmRepository extends CassandraRepository<Alarm, Alarm.Key> {
    List<Alarm> findByPartitionKeyOrganizationIdAndPartitionKeyParamId(Long orgId, Long paramId);
}
