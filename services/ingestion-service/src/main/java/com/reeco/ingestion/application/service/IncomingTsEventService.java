package com.reeco.ingestion.application.service;

import com.reeco.ingestion.application.port.in.IncomingTsEvent;
import com.reeco.ingestion.application.port.out.IndicatorRepository;
import com.reeco.ingestion.application.port.out.InsertEventPort;
import com.reeco.ingestion.application.usecase.StoreTsEventUseCase;
import com.reeco.ingestion.domain.Indicator;
import com.reeco.ingestion.domain.NumericalTsEvent;
import com.reeco.ingestion.domain.CategoricalTsEvent;
import com.reeco.ingestion.utils.annotators.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@UseCase
@RequiredArgsConstructor
@Log4j2
public class IncomingTsEventService implements StoreTsEventUseCase {

    private final InsertEventPort tsEventRepository;
    private final IndicatorRepository indicatorRepository;

    @Override
    public void storeEvent(IncomingTsEvent event) {
        indicatorRepository.findById(event.getIndicatorId())
                .switchIfEmpty(Mono.defer(() -> {
                    log.error("No Indicator found with Id [{}]", event.getIndicatorId());
                    return Mono.empty();}))
                .flatMap(v -> {
                    if (v.getValueType() == Indicator.ValueType.NUMBER) {
                        // TODO : Implement event mapper
                        NumericalTsEvent numTsEvent = new NumericalTsEvent(
                                event.getOrganizationId(),
                                event.getStationId(),
                                event.getParamId(),
                                event.getEventTime(),
                                event.getIndicatorName(),
                                event.getParamName(),
                                event.getConnectionId(),
                                event.getReceivedAt(),
                                event.getLat(),
                                event.getLon(),
                                Double.valueOf(event.getValue())
                        );
                        return tsEventRepository.insertNumericEvent(numTsEvent);
                    } else {
                        // TODO : Implement event mapper
                        CategoricalTsEvent catTsEvent = new CategoricalTsEvent(
                                event.getOrganizationId(),
                                event.getStationId(),
                                event.getParamId(),
                                event.getEventTime(),
                                event.getIndicatorName(),
                                event.getParamName(),
                                event.getConnectionId(),
                                event.getReceivedAt(),
                                event.getLat(),
                                event.getLon(),
                                event.getValue()
                        );
                        return tsEventRepository.insertCategoricalEvent(catTsEvent);
                    }
                }).subscribe(log::info);
    }


}
