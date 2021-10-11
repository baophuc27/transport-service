package com.reeco.ingestion.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NumericTsEvent {


    Long stationId;

    LocalDateTime timeStamp;

    String metric;

    Double value;

    Long deviceId;

    LocalDateTime receivedAt;

    Double lat;

    Double lon;

    // TODO : handle converting to standard unit

}
