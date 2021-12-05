package com.reeco.ingestion.infrastructure.persistence.cassandra.repository;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.AlarmInfo;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.EventStatisticMetaInfo;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventStatisticInfoRepository extends ReactiveCassandraRepository<EventStatisticMetaInfo, EventStatisticMetaInfo.Key> {

}
