package com.reeco.core.dmp.core.model.cassandra;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table("numeric_stats_by_organization")
@Data
@AllArgsConstructor
public class NumericalStatByOrg {
    @PrimaryKeyClass
    @Data
    @AllArgsConstructor
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "organization_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long organizationId;

        @PrimaryKeyColumn(name = "date", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
        private LocalDate date;

        @PrimaryKeyColumn(name = "param_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private Long paramId;
    }

    @PrimaryKey
    private NumericalStatByOrg.Key partitionKey;

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

    @Column("is_alarm")
    private Boolean isAlarm;

    @Column("alarm_id")
    private Long alarmId;

    @Column("alarm_type")
    private String alarmType;
}
