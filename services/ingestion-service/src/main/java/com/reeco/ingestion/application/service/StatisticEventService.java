package com.reeco.ingestion.application.service;

import com.reeco.ingestion.application.port.in.IncomingTsEvent;
import com.reeco.ingestion.application.port.out.AggregateEventsPort;
import com.reeco.ingestion.application.usecase.UpdateStatEventUseCase;
import com.reeco.ingestion.utils.annotators.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@UseCase
@RequiredArgsConstructor
@Log4j2
public class StatisticEventService implements UpdateStatEventUseCase {
//
//    private final StatisticsRepository statisticsRepository;
//
//    private final ParameterRepository parameterRepository;

    private final AggregateEventsPort aggregateEventsPort;

    private void aggSum(IncomingTsEvent event) {

    }

    private void aggAvg(IncomingTsEvent event){

    }

    private void aggMedian(IncomingTsEvent event){

    }

    private void aggCount(IncomingTsEvent event){

    }

    @Override
    public void updateNumStatEvent() {
        aggregateEventsPort.insert();
    }

    @Override
    public void updateCatStatEvent() {
    }


}
