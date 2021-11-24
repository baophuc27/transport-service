package com.reeco.ingestion.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IncomingAlarm {
    private Long id;

    private String englishName;

    private String vietnameseName;

    private String alarmType;

    private String minValue;

    private String maxValue;

    private String maintainType;

    private Long numOfMatch;

    private Long frequence;

    private String frequenceType;
}
