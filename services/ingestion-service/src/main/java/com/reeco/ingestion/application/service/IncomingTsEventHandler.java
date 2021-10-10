package com.reeco.ingestion.application.service;

import com.reeco.ingestion.application.port.out.MetricRepository;
import com.reeco.ingestion.application.port.out.TsEventRepository;
import com.reeco.ingestion.application.usecase.StoreTsEventUseCase;
import com.reeco.ingestion.domain.Metric;
import com.reeco.ingestion.domain.NumericTsEvent;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.MetricByStation;
import com.reeco.ingestion.utils.annotators.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@UseCase
@RequiredArgsConstructor
@Log4j2
public class IncomingTsEventHandler implements StoreTsEventUseCase {

    private final TsEventRepository tsEventRepository;
    private final MetricRepository metricRepository;

    @Override
    public void storeEvent(NumericTsEvent event) {
        metricRepository.loadMetricInfoByStation(
                event.getStationId(),
                event.getDeviceId(),
                event.getMetric())
                .handle((v, sink) -> {
                    if (v.getValueType() == Metric.ValueType.DOUBLE) {
                        tsEventRepository.insertNumericEvent(event);
                    } else {
                        tsEventRepository.insertTextEvent(event);
                    }
                });
    }
}
