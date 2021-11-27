package com.reeco.core.dmp.core.repo;

import com.reeco.core.dmp.core.model.NumericalStatByOrg;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NumericalStatByOrgRepository extends CassandraRepository<NumericalStatByOrg, NumericalStatByOrg.Key> {
//    @NotNull
//    @Override
//    <S extends NumericalStatByOrg> Flux<S> saveAll(Publisher<S> entityStream);
//
//    @Override
//    <S extends NumericalStatByOrg> Mono<S> save(S entity);
    @Query("select * from numeric_stats_by_organization\n" +
            "where organization_id=?0\n" +
            "and param_id = ?1\n" +
            "and date>= ?2\n" +
            "and date<= ?3\n" +
            "ALLOW FILTERING")
    List<NumericalStatByOrg> findNumericDataDate(Long orgId, Long paramId, LocalDate startTime, LocalDate endTime);
}
