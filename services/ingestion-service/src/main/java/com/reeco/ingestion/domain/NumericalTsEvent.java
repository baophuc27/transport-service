package com.reeco.ingestion.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

public class NumericalTsEvent extends BaseEvent{

    private Double value;

    public NumericalTsEvent(Long organizationId,
                            Long stationId,
                            Long paramId,
                            LocalDateTime timeStamp,
                            String metric, Long deviceId, LocalDateTime receivedAt, Double lat, Double lon, Double value) {
        super(organizationId, stationId, paramId, timeStamp, metric, deviceId, receivedAt, lat, lon);
        this.value = value;
    }

    // TODO : handle converting to standard unit

}
