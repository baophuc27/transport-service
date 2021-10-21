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

    Long indicator_id;
    Long group_id;
    String group_name;
    String indicator_name;
    ValueType valueType;
    String unit;
    String standardUnit;
}
