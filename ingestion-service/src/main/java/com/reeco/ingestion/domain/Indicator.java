package com.reeco.ingestion.domain;

import com.reeco.common.model.enumtype.ValueType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Indicator {

    Long indicatorId;
    Long groupId;
    String groupName;
    String indicatorName;
    ValueType valueType;
    String unit;
    String standardUnit;
}
