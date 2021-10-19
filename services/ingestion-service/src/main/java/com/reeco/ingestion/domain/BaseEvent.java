package com.reeco.ingestion.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BaseEvent {
    private Long organizationId;

    private Long stationId;

    private Long paramId;

    private LocalDateTime eventTime;

    private String indicatorName;

    private Long deviceId;

    private LocalDateTime receivedAt;

    private Double lat;

    private Double lon;
}
