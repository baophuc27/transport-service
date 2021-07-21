package com.marvin.core.dmp.application.port.out;

import com.marvin.core.dmp.application.domain.governance.View;
import reactor.core.publisher.Mono;


public interface ViewRepository {

    Mono<View> save(View view);

    Mono<View> findById(int viewId);

    Mono<Void> deleteById(int viewId);
}
