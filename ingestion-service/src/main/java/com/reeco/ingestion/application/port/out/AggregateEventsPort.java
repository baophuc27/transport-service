package com.reeco.ingestion.application.port.out;

import com.reeco.ingestion.domain.CategoricalStatEvent;
import com.reeco.ingestion.domain.NumericalStatEvent;
import reactor.core.publisher.Flux;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface AggregateEventsPort {

    Flux<NumericalStatEvent> aggEventByOrgAndParams(LocalDateTime endTime);

    Flux<List<CategoricalStatEvent>> aggCatEventByOrgAndParams(Timestamp startTime, Timestamp endTime);
}
