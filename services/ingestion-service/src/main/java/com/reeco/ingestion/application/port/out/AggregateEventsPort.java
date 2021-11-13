package com.reeco.ingestion.application.port.out;

import com.reeco.ingestion.domain.NumericalStatEvent;
import reactor.core.publisher.Flux;

import java.sql.Timestamp;

public interface AggregateEventsPort {

    Flux<NumericalStatEvent> aggEventByOrgAndParams(Timestamp startTime, Timestamp endTime);


}
