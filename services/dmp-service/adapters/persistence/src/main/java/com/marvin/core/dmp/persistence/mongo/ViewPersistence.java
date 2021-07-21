package com.marvin.core.dmp.persistence.mongo;

import com.marvin.core.dmp.application.domain.governance.View;
import com.marvin.core.dmp.application.port.out.ViewRepository;
import reactor.core.publisher.Mono;


public class ViewPersistence implements ViewRepository {

    private final ReactiveMongoViewRepository repository;

    public ViewPersistence(final ReactiveMongoViewRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<View> save(View view) {
        return this.repository.save(view);
    }


    @Override
    public Mono<View> findById(int accountId) {
        return this.repository.findByViewId(accountId);
    }

    @Override
    public Mono<Void> deleteById(int accountId) {
        return this.repository.deleteByViewId(accountId);
    }
}
