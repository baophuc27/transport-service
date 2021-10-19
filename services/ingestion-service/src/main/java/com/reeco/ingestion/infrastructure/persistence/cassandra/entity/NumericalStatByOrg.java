package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;

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
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "organization_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long organizationId;

        @PrimaryKeyColumn(name = "date", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
        private String date;

        @PrimaryKeyColumn(name = "param_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private Long paramId;
    }

    @PrimaryKey
    private CategoricalStatByOrg.Key partitionKey;

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
    LocalDateTime lastUpdated;
}
