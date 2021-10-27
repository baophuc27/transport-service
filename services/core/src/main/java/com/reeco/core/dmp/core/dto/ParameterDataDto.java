package com.reeco.core.dmp.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;

@Data
@NoArgsConstructor
public class ParameterDataDto {
    private ParameterDto parameterDto;

    private List<DataPointDto> dataPointDtos;
}
