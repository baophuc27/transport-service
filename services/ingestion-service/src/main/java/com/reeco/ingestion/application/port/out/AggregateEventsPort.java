package com.reeco.ingestion.application.port.out;

import com.reeco.ingestion.domain.CategoricalTsEvent;
import com.reeco.ingestion.domain.NumericalTsEvent;
import com.reeco.ingestion.domain.Parameter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface AggregateEventsPort {
    void insert();

    Flux<Parameter.ParamsByOrg> findAllParamsGroupByOrg();

    Mono<NumericalTsEvent> aggEvents(Long orgId, List<Long> paramIds);

}
