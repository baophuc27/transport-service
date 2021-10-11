package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;

@Table("metrics_by_station")
@AllArgsConstructor
@Getter
public class MetricByStation {


    @PrimaryKeyClass
    @AllArgsConstructor
    @Getter
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "station_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long stationId;

        @PrimaryKeyColumn(name = "device_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Long deviceId;

        @PrimaryKeyColumn(name = "metric", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
        private String metric;
    }

    @PrimaryKey
    private MetricByStation.Key partitionKey;

    @Column("value_type")
    private String valueType;

    @Column("unit")
    private String unit;

    @Column("standard_unit")
    private String standardUnit;
}
