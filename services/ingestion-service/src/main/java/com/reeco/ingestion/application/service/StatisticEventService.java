package com.reeco.ingestion.application.service;

import com.reeco.ingestion.application.port.out.NumStatRepository;
import com.reeco.ingestion.application.usecase.StatisticEventUseCase;
import com.reeco.ingestion.utils.annotators.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@UseCase
@RequiredArgsConstructor
@Log4j2
public class StatisticEventService implements StatisticEventUseCase {

    private final NumStatRepository numStatRepository;
//
//    private final ParameterRepository parameterRepository;

//    private final AggregateEventsPort aggregateEventPort;
//
//    private final LoadOrgAndParamPort loadOrgAndParamPort;

    @Override
    public void updateNumStatEvent(Timestamp startTime, Timestamp endTime) {
        numStatRepository.insert(startTime, endTime);
    }

    @Override
    public void updateCatStatEvent() {
    }


}
