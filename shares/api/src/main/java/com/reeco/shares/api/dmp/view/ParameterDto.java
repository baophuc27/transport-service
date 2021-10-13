package com.reeco.shares.api.dmp.view;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class ParameterDto {

    private Long parameterId;

    private String parameterName;

    private String indicatorName;

    private String indicatorType;

    private String indicatorKey;

    private String unit;
}
