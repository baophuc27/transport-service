package com.reeco.ingestion.domain;

import com.reeco.common.model.enumtype.AlarmType;
import lombok.*;
import org.springframework.data.cassandra.core.mapping.Column;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class BaseEvent {
    private Long organizationId;

    private Long workspaceId;

    private Long connectionId;

    private Long stationId;

    private Long paramId;

    private LocalDateTime eventTime;

    private Long indicatorId;

    private String indicatorName;

    private String paramName;

    private LocalDateTime receivedAt;

    private LocalDateTime sentAt;

    private Double lat; // nullable

    private Double lon;

    private Boolean isAlarm;

    private Long alarmId;

    private AlarmType alarmType;

    private String minValue;

    private String maxValue;
}
