package com.marvin.core.dmp.persistence.mongo;

import com.marvin.core.dmp.application.domain.governance.View;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveMongoViewRepository extends ReactiveMongoRepository<View,String> {

    Mono<View> findByViewId(int viewId);

    Mono<Void> deleteByViewId(int viewId);
}
