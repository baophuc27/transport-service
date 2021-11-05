package com.reeco.core.dmp.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ParameterDto {
    private Long organizationId;

    private Long stationId;

    private Long parameterId;

    private String parameterName;

    private String indicatorName;

    private String indicatorType;

    private String indicatorKey;

    private String unit;
}
