package com.reeco.ingestion.adapter.out;

import com.reeco.ingestion.application.port.out.MetricRepository;
import com.reeco.ingestion.application.port.out.TsEventRepository;
import com.reeco.ingestion.domain.Metric;
import com.reeco.ingestion.domain.NumericTsEvent;
import com.reeco.ingestion.domain.TextTsEvent;
import com.reeco.ingestion.domain.UnitConverter;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.*;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.MetricByStationRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.TsByMetricRepository;
import com.reeco.ingestion.utils.annotators.Adapter;
import com.reeco.ingestion.utils.exception.EventProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.cassandra.core.ReactiveCassandraBatchOperations;
import org.springframework.data.cassandra.core.ReactiveCassandraTemplate;
import org.springframework.data.cassandra.core.WriteResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Adapter
@RequiredArgsConstructor
@Log4j2
public class TsEventPersistenceAdapter implements TsEventRepository, MetricRepository {

    private final MetricByStationRepository metricByStationRepository;

    private final TsByMetricRepository tsByMetricRepository;

    private final ReactiveCassandraTemplate reactiveCassandraTemplate;


    @Override
    public void insertNumericEvent(NumericTsEvent event) {
        final ReactiveCassandraBatchOperations batchOps = reactiveCassandraTemplate.batchOps();
        insertNumSeriesByStation(event, batchOps);
        insertNumSeriesByMetric(event, batchOps);
        Mono<WriteResult> writeResult = batchOps.execute();
        writeResult.map(WriteResult::getRows).flatMapMany(Flux::fromIterable).log().subscribe();
    }

    @Override
    public void insertTextEvent(TextTsEvent event) {
        final ReactiveCassandraBatchOperations batchOps = reactiveCassandraTemplate.batchOps();
        insertTextSeriesByStation(event, batchOps);
        insertTextSeriesByMetric(event, batchOps);
        Mono<WriteResult> writeResult = batchOps.execute();
        writeResult.map(WriteResult::getRows).flatMapMany(Flux::fromIterable).log().subscribe();
    }

    private void insertNumSeriesByStation(NumericTsEvent event, ReactiveCassandraBatchOperations batchOps) {
        NumTsByStation.Key partitionKey = new NumTsByStation.Key(
                event.getStationId(),
                event.getDeviceId(),
                event.getTimeStamp(),
                event.getMetric());

        batchOps.insert(new NumTsByStation(partitionKey, event.getValue()));
    }

    private void insertNumSeriesByMetric(NumericTsEvent event, ReactiveCassandraBatchOperations batchOps) {
        NumTsByMetric.Key partitionKey = new NumTsByMetric.Key(
                event.getStationId(),
                event.getMetric(),
                event.getTimeStamp(),
                event.getDeviceId()
        );
        batchOps.insert(new NumTsByMetric(partitionKey, event.getValue()));
    }

    private void insertTextSeriesByStation(TextTsEvent event, ReactiveCassandraBatchOperations batchOps) {
        TextTsByStation.Key partitionKey = new TextTsByStation.Key(
                event.getStationId(),
                event.getDeviceId(),
                event.getTimeStamp(),
                event.getMetric(),
                event.getValue());

        batchOps.insert(new TextTsByStation(partitionKey, event.getValue()));
    }

    private void insertTextSeriesByMetric(TextTsEvent event, ReactiveCassandraBatchOperations batchOps) {
        TextTsByMetric.Key partitionKey = new TextTsByMetric.Key(
                event.getStationId(),
                event.getMetric(),
                event.getTimeStamp(),
                event.getValue(),
                event.getDeviceId(),);

        batchOps.insert(new TextTsByMetric(partitionKey, event.getValue()));
    }


    @Override
    public Mono<Metric> loadMetricInfoByStation(Long stationId, Long deviceId, String metricName) {
        MetricByStation.Key partitionKey = new MetricByStation.Key(stationId, deviceId, metricName);
        return metricByStationRepository
                .findById(Mono.just(partitionKey))
                .switchIfEmpty(Mono.error(new EventProcessingException("Metric with Key: " + partitionKey.toString() + " not found!")))
                .map(
                        // TODO: implement Metric mapper
                        v -> new Metric(v.getPartitionKey().getStationId(),
                                v.getPartitionKey().getDeviceId(),
                                v.getPartitionKey().getMetric(),
                                Metric.ValueType.valueOf(v.getValueType()),
                                v.getUnit(),
                                v.getStandardUnit())
                );
    }
}
