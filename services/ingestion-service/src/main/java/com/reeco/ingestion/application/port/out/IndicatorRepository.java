package com.reeco.ingestion.application.port.out;

import com.reeco.ingestion.domain.Indicator;
import com.reeco.ingestion.domain.Parameter;
import reactor.core.publisher.Mono;

public interface IndicatorRepository {
    Mono<Indicator> findById(Long indicatorId);
}
