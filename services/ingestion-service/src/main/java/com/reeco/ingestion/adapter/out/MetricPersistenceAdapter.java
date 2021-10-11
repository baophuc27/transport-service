package com.reeco.ingestion.adapter.out;

import com.reeco.ingestion.application.port.out.MetricRepository;
import com.reeco.ingestion.domain.Metric;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.MetricByStationRepository;
import reactor.core.publisher.Mono;

public class MetricPersistenceAdapter implements MetricRepository {

    MetricByStationRepository metricByStationRepository;


    @Override
    public Mono<Metric> loadMetricInfoByStation(Long stationId, Long deviceId, String metricName) {
        return null;
    }
}