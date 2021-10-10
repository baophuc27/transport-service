package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;

@Table("devices_by_station")
@Data
public class DeviceByStation {

    @PrimaryKeyClass
    @Data
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "station_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long stationId;

        @PrimaryKeyColumn(name = "device_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Long deviceId;

        @PrimaryKeyColumn(name = "status", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
        private Boolean status;
    }

    @PrimaryKey
    private DeviceByStation.Key partitionKey;

    @Column("value")
    private String value;

    @Column("lat")
    private Double lat;

    @Column("lon")
    private Double lon;

    @Column("description")
    private String description;

}
