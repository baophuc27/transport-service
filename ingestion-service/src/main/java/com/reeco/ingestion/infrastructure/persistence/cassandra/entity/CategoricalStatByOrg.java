package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table("categorical_stats_by_organization")
@Data
public class CategoricalStatByOrg {

    @PrimaryKeyClass
    @Data
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "organization_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long organizationId;

        @PrimaryKeyColumn(name = "date", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private String date;

        @PrimaryKeyColumn(name = "param_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private Long paramId;

        @PrimaryKeyColumn(name = "value", ordinal = 3, type = PrimaryKeyType.CLUSTERED)
        private String value;
    }

    @PrimaryKey
    private CategoricalStatByOrg.Key partitionKey;

    @Column("value_count")
    private Long valueCount;

    @Column("last_updated")
    private LocalDateTime lastUpdated;
}
