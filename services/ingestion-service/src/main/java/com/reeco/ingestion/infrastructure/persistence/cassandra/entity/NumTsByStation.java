package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;


import lombok.AllArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table("numeric_series_by_station")
@AllArgsConstructor
public class NumTsByStation {

    @PrimaryKeyClass
    @AllArgsConstructor
    public static class Key implements Serializable {
        @PrimaryKeyColumn(name = "station_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long stationId;

        @PrimaryKeyColumn(name = "device_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
        private Long deviceId;

        @PrimaryKeyColumn(name = "event_time", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private LocalDateTime eventTime;

        @PrimaryKeyColumn(name = "metric", ordinal = 3, type = PrimaryKeyType.CLUSTERED)
        private String metric;

    }

    @PrimaryKey
    private NumTsByStation.Key partitionKey;

    @Column("value")
    private Double value;

    @Column("inserted_time")
    private LocalDateTime insertedTime;

    @Column("kafka_ts")
    private LocalDateTime kafkaTime;

    @Column("lat")
    private Double lat;

    @Column("lon")
    private Double lon;

}
