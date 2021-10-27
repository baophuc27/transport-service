package com.reeco.core.dmp.core.repo;

import com.reeco.core.dmp.core.model.Indicator;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public interface IndicatorInfoRepository extends CassandraRepository<Indicator, Indicator.Key> {
    Optional<Indicator> findById(Indicator.Key key);

    Optional<Indicator> findByPartitionKeyIndicatorId(Long id);
}
