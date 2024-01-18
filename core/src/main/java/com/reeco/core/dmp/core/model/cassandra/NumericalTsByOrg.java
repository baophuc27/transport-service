package com.reeco.core.dmp.core.model.cassandra;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table("numeric_series_by_organization")
@AllArgsConstructor
@Data
public class NumericalTsByOrg {

    @PrimaryKeyClass
    @AllArgsConstructor
    @Data
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "organization_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long organizationId;

//        @PrimaryKeyColumn(name = "date", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
//        private LocalDate date;

        @PrimaryKeyColumn(name = "event_time", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
        private LocalDateTime eventTime;

        @PrimaryKeyColumn(name = "param_id", ordinal = 3, type = PrimaryKeyType.CLUSTERED)
        private Long paramId;


        @Override
        public String toString() {
            return "Key{" +
                    "organizationId=" + organizationId +
//                    ", date=" + date +
                    ", eventTime=" + eventTime +
                    ", paramId=" + paramId +
                    '}';
        }
    }

    @PrimaryKey
    private NumericalTsByOrg.Key partitionKey;

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
    private Double value;

    @Column("received_at")
    private LocalDateTime receivedAt;

    @Column("is_alarm")
    private Boolean isAlarm;

    @Column("alarm_type")
    private String alarmType;

    @Column("alarm_id")
    private Long alarmId;

    @Column("lat")
    private Double lat;

    @Column("lon")
    private Double lon;

    @Override
    public String toString() {
        return "NumericalTsByOrg{" +
                "partitionKey=" + partitionKey.toString() +
                ", indicatorName='" + indicatorName + '\'' +
                ", paramName='" + paramName + '\'' +
                ", date=" + date.toString() +
                ", stationId=" + stationId +
                ", connectionId=" + connectionId +
                ", value=" + value +
                ", receivedAt=" + receivedAt +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
