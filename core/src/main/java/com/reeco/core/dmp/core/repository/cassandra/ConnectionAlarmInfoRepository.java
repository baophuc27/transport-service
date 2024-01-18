package com.reeco.core.dmp.core.repository.cassandra;

import com.reeco.core.dmp.core.annotations.Traceable;
import com.reeco.core.dmp.core.model.cassandra.ConnectionAlarmInfo;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionAlarmInfoRepository extends CassandraRepository<ConnectionAlarmInfo, ConnectionAlarmInfo.ConnectionAlarmKey> {

    @Traceable
    @Query("select * from reecotech.connection_alarm\n" +
            "where organization_id = ?0 and connection_id = ?1 order by organization_id, alarm_time")
    List<ConnectionAlarmInfo> findHistoryByConnection(Long orgId, Long connectionId);
}
