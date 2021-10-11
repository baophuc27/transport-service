package com.reeco.ingestion.application.port.in;

import com.reeco.ingestion.domain.Metric;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class IncomingTsEvent {

    Long stationId;

    LocalDateTime timeStamp;

    String metric;

    String value;

    Long deviceId;

    LocalDateTime receivedAt;

    Double lat;

    Double lon;

}
