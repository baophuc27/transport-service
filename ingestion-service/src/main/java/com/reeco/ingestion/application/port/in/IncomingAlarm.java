package com.reeco.ingestion.application.port.in;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
//@Getter
//@Builder
@NoArgsConstructor
public class IncomingAlarm {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("englishName")
    private String englishName;

    @JsonProperty("vietnameseName")
    private String vietnameseName;

    @JsonProperty("alarmType")
    private String alarmType;

    @JsonProperty("minValue")
    private String minValue;

    @JsonProperty("maxValue")
    private String maxValue;

    @JsonProperty("maintainType")
    private String maintainType;

    @JsonProperty("numOfMatch")
    private Long numOfMatch;

    @JsonProperty("frequence")
    private Long frequence;

    @JsonProperty("frequenceType")
    private String frequenceType;
}
