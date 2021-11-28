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

@Table("categorical_series_by_organization")
@AllArgsConstructor
@Data
public class CategoricalTsByOrg {

    @PrimaryKeyClass
    @AllArgsConstructor
    @Data
    public static class Key implements Serializable {
        @PrimaryKeyColumn(name = "organization_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long organizationId;

        @PrimaryKeyColumn(name = "param_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Long paramId;

        @PrimaryKeyColumn(name = "event_time", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private LocalDateTime eventTime;

        @PrimaryKeyColumn(name = "value", ordinal = 3, type = PrimaryKeyType.CLUSTERED)
        private String value;

    }
    @PrimaryKey
    private CategoricalTsByOrg.Key partitionKey;

    @Column("indicator_id")
    Long indicatorId;

    @Column("indicator_name")
    private String indicatorName;

    @Column("param_name")
    private String paramName;

    @Column("date")
    private LocalDate date;

    @Column("station_id")
    private Long stationId;

    @Column("connection_id")
    private Long connectionId;

    @Column("value")
    private String value;

    @Column("received_at")
    private LocalDateTime receivedAt;

    @Column("lat")
    private Double lat;

    @Column("lon")
    private Double lon;

    @Column("sent_at")
    LocalDateTime sentAt;

    @Column("is_alarm")
    Boolean isAlarm;

    @Column("alarm_id")
    Long alarmId;

    @CassandraType(type = CassandraType.Name.TEXT)
    @Column("alarm_type")
    AlarmType alarmType;

    @Column("min_value")
    String minValue;

    @Column("max_value")
    String maxValue;

}
