package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Table("text_statistics_by_device")
@Data
public class TextStatByDevice {

    @PrimaryKeyClass
    @Data
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "device_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long deviceId;

        @PrimaryKeyColumn(name = "metric", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
        private String metric;

        @PrimaryKeyColumn(name = "date", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
        private Date date;

        @PrimaryKeyColumn(name = "value", ordinal = 3, type = PrimaryKeyType.CLUSTERED)
        private String value;
    }

    @PrimaryKey
    private TextStatByDevice.Key partitionKey;

    @Column("value_count")
    private Long valueCount;

    @Column("last_updated")
    private Timestamp last_updated;
}
