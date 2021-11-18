package com.reeco.ingestion.domain;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class BaseEvent {
    private Long organizationId;

    private Long stationId;

    private Long paramId;

    private LocalDateTime eventTime;

    private String indicatorName;

    private String paramName;

    private Long connectionId;

    private LocalDateTime receivedAt;

    private Double lat;

    private Double lon;
}
