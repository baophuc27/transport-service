package com.reeco.ingestion.application.port.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
//@Getter
//@Builder
@NoArgsConstructor
public class Parameter {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("englishName")
    private String englishName;

    @JsonProperty("vietnameseName")
    private String vietnameseName;

    @JsonProperty("parameterType")
    private String parameterType;

    @JsonProperty("indicatorId")
    private Long indicatorId;

    @JsonProperty("keyName")
    private String keyName;

    @JsonProperty("displayType")
    private String displayType;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("format")
    private String format;

    @JsonProperty("alarms")
    private List<IncomingAlarm> alarms;

    public Parameter(Long id,Long indicatorId, String eName, String unit, List<IncomingAlarm> alarms){
        this.id = id;
        this.indicatorId = indicatorId;
        this.englishName = eName;
        this.unit = unit;
        this.alarms = alarms;
    }
}
