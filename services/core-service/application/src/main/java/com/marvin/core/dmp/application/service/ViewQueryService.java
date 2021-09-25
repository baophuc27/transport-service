package com.marvin.core.dmp.application.service;

import com.marvin.core.dmp.application.domain.governance.View;
import com.marvin.core.dmp.application.port.in.ViewQuery;
import com.marvin.core.dmp.application.port.out.ViewRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ViewQueryService implements ViewQuery {

    private final ViewRepository viewRepository;


    @Override
    public Mono<View> getView(int viewId) {
        return viewRepository.findById(viewId);
    }

    @Override
    public Mono<View> saveView(View view) {
        return viewRepository.save(view);
    }

    @Override
    public Mono<Void> deleteView(int viewId) {
        return viewRepository.deleteById(viewId);
    }
}
