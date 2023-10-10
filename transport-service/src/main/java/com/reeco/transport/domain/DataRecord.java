package com.reeco.transport.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
public class DataRecord {

    LocalDateTime timeStamp;

    String key;

    Double value;

    Integer deviceId;

    LocalDateTime receivedAt;

    Double lat;

    Double lon;


    public Integer getDeviceId() {
        return deviceId;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }
}
