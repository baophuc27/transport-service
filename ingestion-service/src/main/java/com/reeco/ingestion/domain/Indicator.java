package com.reeco.ingestion.domain;

import com.reeco.common.model.enumtype.ValueType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@ToString
public class Indicator {
    Long indicatorId;
    Long groupId;
    String indicatorNameVi;
    String indicatorName;
    ValueType valueType;
    String standardUnit;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public Indicator() {
    }
}
