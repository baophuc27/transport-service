package com.reeco.ingestion.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TextTsEvent {

    Long stationId;

    LocalDateTime timeStamp;

    String metric;

    String value;

    Long deviceId;

    LocalDateTime receivedAt;

    Double lat;

    Double lon;
}
