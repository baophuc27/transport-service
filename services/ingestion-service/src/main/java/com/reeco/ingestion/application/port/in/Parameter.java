package com.reeco.ingestion.application.port.in;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Parameter {

    private Long id;

    private String englishName;

    private String vietnameseName;

    private String parameterType;

    private Long indicatorId;

    private String keyName;

    private String displayType;

    private String unit;

    private String format;

    private List<IncomingAlarm> alarms;

    public Parameter(Long id,Long indicatorId, String eName, String unit, List<IncomingAlarm> alarms){
        this.id = id;
        this.indicatorId = indicatorId;
        this.englishName = eName;
        this.unit = unit;
        this.alarms = alarms;
    }
}
