package com.reeco.ingestion.application.service;

import com.reeco.ingestion.application.port.in.IncomingTsEvent;
import com.reeco.ingestion.application.port.out.MetricRepository;
import com.reeco.ingestion.application.port.out.TsEventRepository;
import com.reeco.ingestion.application.usecase.StoreTsEventUseCase;
import com.reeco.ingestion.domain.Metric;
import com.reeco.ingestion.domain.NumericTsEvent;
import com.reeco.ingestion.domain.TextTsEvent;
import com.reeco.ingestion.utils.annotators.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@UseCase
@RequiredArgsConstructor
@Log4j2
public class IncomingTsEventHandler implements StoreTsEventUseCase {

    private final TsEventRepository tsEventRepository;
    private final MetricRepository metricRepository;

    @Override
    public void storeEvent(IncomingTsEvent event) {

        metricRepository.loadMetricInfoByStation(
                event.getStationId(),
                event.getDeviceId(),
                event.getMetric())
                .doOnNext(v -> {
                    if (v.getValueType() == Metric.ValueType.DOUBLE) {
                        // TODO : Implement event mapper
                        NumericTsEvent numericTsEvent = new NumericTsEvent(
                                event.getStationId(),
                                event.getTimeStamp(),
                                event.getMetric(),
                                Double.valueOf(event.getValue()),
                                event.getDeviceId(),
                                event.getReceivedAt(),
                                event.getLat(),
                                event.getLon()
                        );
                        tsEventRepository.insertNumericEvent(numericTsEvent);
                    } else if (v.getValueType() == Metric.ValueType.STRING) {
                        // TODO : Implement event mapper
                        TextTsEvent textTsEvent = new TextTsEvent(
                                event.getStationId(),
                                event.getTimeStamp(),
                                event.getMetric(),
                                event.getValue(),
                                event.getDeviceId(),
                                event.getReceivedAt(),
                                event.getLat(),
                                event.getLon()
                        );
                        tsEventRepository.insertTextEvent(textTsEvent);
                    }
                }).log().subscribe();
    }
}
