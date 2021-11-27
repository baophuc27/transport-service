package com.reeco.common.shares.api.dmp.view;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParameterDataDto {
    private ParameterDto parameterDto;

    private List<DataPointDto> dataPointDtos;
}
