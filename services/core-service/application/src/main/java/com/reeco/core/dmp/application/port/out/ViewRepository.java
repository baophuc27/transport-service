package com.reeco.core.dmp.application.port.out;

import com.reeco.core.dmp.application.domain.governance.View;
import reactor.core.publisher.Mono;


public interface ViewRepository {

    Mono<View> save(View view);

    Mono<View> findById(int viewId);

    Mono<Void> deleteById(int viewId);
}
