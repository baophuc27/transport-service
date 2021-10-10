package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Table("numeric_statistics_by_device")
@Data
public class NumStatByDevice {
    @PrimaryKeyClass
    @Data
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "device_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long deviceId;

        @PrimaryKeyColumn(name = "metric", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
        private String metric;

        @PrimaryKeyColumn(name = "date", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private Date date;
    }

    @PrimaryKey
    private TextStatByDevice.Key partitionKey;

    @Column("min")
    private Double min;

    @Column("max")
    private Double max;

    @Column("median")
    private Double median;

    @Column("mean")
    private Double mean;

    @Column("acc")
    private Double acc;

    @Column("std")
    private Double std;

    @Column("count")
    private Long count;

    @Column("last_updated")
    LocalDateTime last_updated;
}
