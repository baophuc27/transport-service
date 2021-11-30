package com.reeco.core.dmp.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Data
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParameterDto {
    private Long organizationId;

    private Long stationId;

    private Long parameterId;

    private String parameterName;

    private String indicatorName;

    private String indicatorType;

    private String indicatorKey;

    private String unit;

    private String columnKey;

    private Long connectionId;
}
