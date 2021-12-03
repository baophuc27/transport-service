package com.reeco.ingestion.domain;

import com.reeco.common.model.enumtype.AlarmType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class CategoricalTsEvent extends BaseEvent {

    @Getter
    @Setter
    String value;

    public CategoricalTsEvent(Long organizationId, Long workspaceId, Long connectionId, Long stationId, Long paramId, LocalDateTime eventTime, Long indicatorId, String indicatorName, String paramName, LocalDateTime receivedAt, LocalDateTime sentAt, Double lat, Double lon, Boolean isAlarm, Long alarmId, AlarmType alarmType, String minValue, String maxValue, String value) {
        super(organizationId, workspaceId, connectionId, stationId, paramId, eventTime, indicatorId, indicatorName, paramName, receivedAt, sentAt, lat, lon, isAlarm, alarmId, alarmType, minValue, maxValue);
        this.value = value;
    }
}
