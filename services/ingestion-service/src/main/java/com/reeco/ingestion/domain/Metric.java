package com.reeco.ingestion.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Metric {

    public enum ValueType{
        DOUBLE, STRING
    }

    private Long stationId;
    private Long deviceId;
    private String metric;
    private ValueType valueType;
    private String unit;
    private String standardUnit;
}
