package com.reeco.ingestion.adapter.out;

import com.reeco.ingestion.application.port.out.MetricRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.MetricByStationRepository;

public class MetricPersistenceAdapter implements MetricRepository {

    MetricByStationRepository metricByStationRepository;

    @Override
    public void delete(String deviceId){
        // implement this
    }

    @Override
    public void save(String deviceConfig) {
        // implement this
    }
}