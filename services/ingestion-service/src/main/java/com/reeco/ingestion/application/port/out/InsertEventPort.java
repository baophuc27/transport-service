package com.reeco.ingestion.application.port.out;

import com.reeco.ingestion.domain.NumericalTsEvent;
import com.reeco.ingestion.domain.CategoricalTsEvent;
import reactor.core.publisher.Mono;

public interface InsertEventPort {
    Mono<NumericalTsEvent> insertNumericEvent(NumericalTsEvent e);

    Mono<CategoricalTsEvent> insertCategoricalEvent(CategoricalTsEvent e);

}
