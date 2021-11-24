package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;

@Table("alarm")
@AllArgsConstructor
@Getter
public class AlarmInfo {
    @PrimaryKeyClass
    @AllArgsConstructor
    @Getter
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "organization_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long organizationId;

        @PrimaryKeyColumn(name = "param_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Long paramId;

        @PrimaryKeyColumn(name = "alarm_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
        private Long alarmId;
    }

    @PrimaryKey
    private AlarmInfo.Key partitionKey;

    @Column("alarm_type")
    private String alarmType;

    @Column("min_value")
    private String minValue;

    @Column("max_value")
    private String maxValue;

    private Long numOfMatch;

    private Long frequency;

    private String frequencyType;

}
