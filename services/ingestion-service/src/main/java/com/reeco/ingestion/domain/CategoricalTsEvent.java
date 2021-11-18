package com.reeco.ingestion.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class CategoricalTsEvent extends BaseEvent {

    @Getter
    @Setter
    String value;

    public CategoricalTsEvent(Long organizationId,
                              Long stationId,
                              Long paramId,
                              LocalDateTime eventTime,
                              String indicatorName,
                              String paramName,
                              Long connectionId,
                              LocalDateTime receivedAt,
                              Double lat,
                              Double lon,
                              String value) {
        super(organizationId, stationId, paramId, eventTime, indicatorName, paramName, connectionId, receivedAt, lat, lon);
        this.value = value;
    }
}
