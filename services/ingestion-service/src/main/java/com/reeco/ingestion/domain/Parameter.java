package com.reeco.ingestion.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Parameter {
    private Long stationId;
    private Long connectionId;
    private String metric;
    private String unit;
    private String standardUnit;
}
