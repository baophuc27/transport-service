package com.reeco.transport.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DataRecord {
    Integer stationId;

    LocalDateTime timeStamp;

    String key;

    Double value;

    Integer deviceId;

    LocalDateTime receivedAt;

    Double lat;

    Double lon;
}
