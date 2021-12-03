package com.reeco.ingestion.application.port.in;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.reeco.common.model.enumtype.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RuleEngineEvent {

    Long organizationId;

    Long workspaceId;

    Long stationId;

    Long connectionId;

    Long paramId;

    LocalDateTime eventTime;

    Long indicatorId;

    String indicatorName;

    String paramName;

    String value;

    LocalDateTime receivedAt;

    LocalDateTime sentAt;

    Double lat; // nullable

    Double lon;

    Boolean isAlarm;

    Long alarmId;

    AlarmType alarmType;

    String minValue;

    String maxValue;
}
