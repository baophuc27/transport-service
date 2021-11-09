package com.reeco.ingestion.application.port.out;

import com.reeco.ingestion.domain.CategoricalTsEvent;
import com.reeco.ingestion.domain.NumStatisticEvent;
import com.reeco.ingestion.domain.NumericalTsEvent;
import com.reeco.ingestion.domain.Parameter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AggregateEventsPort {

    Flux<NumStatisticEvent> aggEventByOrgAndParams();


}
