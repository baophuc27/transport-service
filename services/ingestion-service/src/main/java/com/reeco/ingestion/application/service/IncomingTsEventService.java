package com.reeco.ingestion.application.service;

import com.reeco.ingestion.application.port.in.IncomingTsEvent;
import com.reeco.ingestion.application.port.out.IndicatorRepository;
import com.reeco.ingestion.application.port.out.TsEventRepository;
import com.reeco.ingestion.application.usecase.StoreTsEventUseCase;
import com.reeco.ingestion.domain.Indicator;
import com.reeco.ingestion.domain.NumericalTsEvent;
import com.reeco.ingestion.domain.CategoricalTsEvent;
import com.reeco.ingestion.utils.annotators.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@UseCase
@RequiredArgsConstructor
@Log4j2
public class IncomingTsEventService implements StoreTsEventUseCase {

    private final TsEventRepository tsEventRepository;
    private final IndicatorRepository indicatorRepository;

    @Override
    public void storeEvent(IncomingTsEvent event) {
//        indicatorRepository.findById(event.getIndicatorId())
//                .switchIfEmpty(Mono.defer(() -> {
//                    log.info("error empty");
//                    return null;
//                }))
//                .doOnNext(v -> {
//                    if (v.getValueType() == Indicator.ValueType.DOUBLE) {
//                        // TODO : Implement event mapper
//                        NumericalTsEvent numTsEvent = new NumericalTsEvent(
//                                event.getOrganizationId(),
//                                event.getStationId(),
//                                event.getParamId(),
//                                event.getEventTime(),
//                                event.getIndicatorName(),
//                                event.getDeviceId(),
//                                event.getReceivedAt(),
//                                event.getLat(),
//                                event.getLon(),
//                                Double.valueOf(event.getValue())
//                        );
//                        tsEventRepository.insertNumericEvent(numTsEvent);
//                    } else if (v.getValueType() == Indicator.ValueType.STRING) {
//                        // TODO : Implement event mapper
//                        CategoricalTsEvent catTsEvent = new CategoricalTsEvent(
//                                event.getOrganizationId(),
//                                event.getStationId(),
//                                event.getParamId(),
//                                event.getEventTime(),
//                                event.getIndicatorName(),
//                                event.getDeviceId(),
//                                event.getReceivedAt(),
//                                event.getLat(),
//                                event.getLon(),
//                                event.getValue()
//                        );
//                        tsEventRepository.insertCategoricalEvent(catTsEvent);
//                    }
//                }).log().subscribe();


        //hard code for test
        Indicator.ValueType valueType = Indicator.ValueType.DOUBLE;
        if (valueType == Indicator.ValueType.DOUBLE) {
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
            tsEventRepository.insertNumericEvent(numTsEvent);
        } else if (valueType == Indicator.ValueType.STRING) {
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
            tsEventRepository.insertCategoricalEvent(catTsEvent);
        }
    }
}
