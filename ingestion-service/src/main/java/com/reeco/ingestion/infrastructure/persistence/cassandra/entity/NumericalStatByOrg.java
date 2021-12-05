package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;

import com.reeco.common.model.enumtype.AlarmType;
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
    public static class NumericalStatKey implements Serializable {

        @PrimaryKeyColumn(name = "organization_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long organizationId;

        @PrimaryKeyColumn(name = "date", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private LocalDate date;

        @PrimaryKeyColumn(name = "param_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
        private Long paramId;
    }

    @PrimaryKey
    private NumericalStatKey partitionKey;

    @Column("min")
    private Double min;

    @Column("max")
    private Double max;

    @Column("mean")
    private Double mean;

    @Column("median")
    private Double median;

    @Column("acc")
    private Double acc;

    @Column("count")
    private Long count;

    @Column("std")
    private Double std;

    @Column("is_alarm")
    private Boolean isAlarm;

    @Column("alarm_id")
    private Long alarmId;

    @Column("alarm_type")
    @CassandraType(type = CassandraType.Name.TEXT)
    private AlarmType alarmType;

    @Column("min_value")
    private String minValue;

    @Column("max_value")
    private String maxValue;

    @Column("last_updated")
    LocalDateTime lastUpdated;
}
