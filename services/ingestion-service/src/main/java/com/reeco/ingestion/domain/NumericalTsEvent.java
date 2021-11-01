package com.reeco.ingestion.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class NumericalTsEvent extends BaseEvent{

    @Getter
    @Setter
    private Double value;

    public NumericalTsEvent(Long organizationId,
                            Long stationId,
                            Long paramId,
                            LocalDateTime timeStamp,
                            String indicatorName, String paramName,
                            Long connectionId,
                            LocalDateTime receivedAt, Double lat, Double lon, Double value) {
        super(organizationId, stationId, paramId, timeStamp, indicatorName, paramName, connectionId, receivedAt, lat, lon);
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
