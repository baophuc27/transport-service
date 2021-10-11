package com.reeco.ingestion.application.port.out;

import com.reeco.ingestion.domain.Metric;
import reactor.core.publisher.Mono;

public interface MetricRepository {
    Mono<Metric> loadMetricInfoByStation(Long stationId, Long deviceId, String metricName);
}
