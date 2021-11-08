package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Table("numeric_stats_by_organization")
@Data
public class NumericalStatByOrg {
    @PrimaryKeyClass
    @Data
    @AllArgsConstructor
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "organization_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long organizationId;

        @PrimaryKeyColumn(name = "date", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private String date;

        @PrimaryKeyColumn(name = "param_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private Long paramId;
    }

    public NumericalStatByOrg(Long organizationId, String date, Long paramId, Double min, Double max, Double mean, Double acc, Long count, LocalDateTime lastUpdated) {
        this.partitionKey = new NumericalStatByOrg.Key(organizationId, date, paramId);
        this.min = min;
        this.max = max;
        this.mean = mean;
        this.acc = acc;
        this.count = count;
        this.lastUpdated = lastUpdated;
    }

    @PrimaryKey
    private NumericalStatByOrg.Key partitionKey;

    @Column("min")
    private Double min;

    @Column("max")
    private Double max;

    @Column("mean")
    private Double mean;

    @Column("acc")
    private Double acc;

    @Column("count")
    private Long count;

    @Column("last_updated")
    LocalDateTime lastUpdated;
}
