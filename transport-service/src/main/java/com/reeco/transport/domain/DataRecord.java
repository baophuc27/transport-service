package com.reeco.transport.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DataRecord {

    LocalDateTime timeStamp;

    String key;

    Double value;

    Integer deviceId;

    LocalDateTime receivedAt;

    Double lat;

    Double lon;
}
