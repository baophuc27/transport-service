package com.reeco.core.dmp.core.repository.cassandra;

import com.reeco.core.dmp.core.annotations.Traceable;
import com.reeco.core.dmp.core.model.cassandra.Indicator;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface IndicatorInfoRepository extends CassandraRepository<Indicator, Indicator.Key> {

    @Traceable
    Optional<Indicator> findById(Indicator.Key key);

    @Traceable
    Optional<Indicator> findByPartitionKeyIndicatorId(Long id);

    @Query("select * from reecotech.indicators where indicator_id = ?0 ALLOW FILTERING")
    Indicator findIndicator(Long indicatorId);
}
