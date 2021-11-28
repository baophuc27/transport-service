package com.reeco.ingestion.domain;

import com.reeco.common.model.enumtype.AlarmType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class NumericalTsEvent extends BaseEvent{

    @Getter
    @Setter
    private Double value;

    public NumericalTsEvent(Long organizationId, Long connectionId, Long stationId, Long paramId, LocalDateTime eventTime, Long indicatorId, String indicatorName, String paramName, Double value, LocalDateTime receivedAt, LocalDateTime sentAt, Double lat, Double lon, Boolean isAlarm, Long alarmId, AlarmType alarmType, String minValue, String maxValue) {
        super(organizationId, connectionId, stationId, paramId, eventTime, indicatorId, indicatorName, paramName, receivedAt, sentAt, lat, lon, isAlarm, alarmId, alarmType, minValue, maxValue);
        this.value = value;
    }

    @Override
    public String toString() {
        return "NumericalTsEvent{" +
                "value=" + value +
                "} " + super.toString();
    }


// TODO : handle converting to standard unit

}
