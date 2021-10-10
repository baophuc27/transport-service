package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;

import lombok.AllArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table("text_series_by_metric")
@AllArgsConstructor
public class TextTsByMetric {

    @PrimaryKeyClass
    @AllArgsConstructor
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "station_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long stationId;

        @PrimaryKeyColumn(name = "metric", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
        private String metric;

        @PrimaryKeyColumn(name = "event_ts", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
        private LocalDateTime eventTs;

        @PrimaryKeyColumn(name = "value", ordinal = 3, type = PrimaryKeyType.CLUSTERED)
        private String value;

        @PrimaryKeyColumn(name = "device_id", ordinal = 4, type = PrimaryKeyType.CLUSTERED)
        private Long deviceId;

    }

    @PrimaryKey
    private TextTsByMetric.Key partitionKey;

    @Column("inserted_time")
    private LocalDateTime insertedTime;

    @Column("kafka_ts")
    private LocalDateTime kafkaTime;

    @Column("lat")
    private Double lat;

    @Column("lon")
    private Double lon;

}
