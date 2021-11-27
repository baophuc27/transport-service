package com.reeco.ingestion.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Indicator {

    public enum ValueType{
        NUMBER, STRING
    }

    Long indicatorId;
    Long groupId;
    String groupName;
    String indicatorName;
    ValueType valueType;
    String unit;
    String standardUnit;
}
