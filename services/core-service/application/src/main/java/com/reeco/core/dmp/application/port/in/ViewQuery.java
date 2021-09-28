package com.reeco.core.dmp.application.port.in;

import com.reeco.core.dmp.application.domain.governance.View;
import reactor.core.publisher.Mono;

public interface ViewQuery {

    public Mono<View> getView(int viewId);

    public  Mono<View> saveView(View view);

    public Mono<Void> deleteView(int viewId);
}
