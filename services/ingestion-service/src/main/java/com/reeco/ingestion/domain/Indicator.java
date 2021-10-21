package com.reeco.ingestion.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class Indicator {

    public enum ValueType{
        DOUBLE, STRING
    }

    Long indicatorId;
    Long groupId;
    String groupName;
    String indicatorName;
    ValueType valueType;
    String unit;
    String standardUnit;
}
