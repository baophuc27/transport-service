package com.reeco.shares.api.dmp.view;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IndicatorDataDto {
    private IndicatorDto indicatorDto;

    private List<DataPointDto> dataPointDtos;
}
