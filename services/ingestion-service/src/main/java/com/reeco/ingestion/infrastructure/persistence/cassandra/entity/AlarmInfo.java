package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    @Column("num_of_match")
    private Long numOfMatch;

    @Column("maintain_type")
    private String maintainType;

    @Column("frequence")
    private Long frequency;

    @Column("frequence_type")
    private String frequencyType;

    @Column("updated_at")
    private LocalDateTime updatedAt;

}
