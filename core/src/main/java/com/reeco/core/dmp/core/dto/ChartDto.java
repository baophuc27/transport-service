package com.reeco.core.dmp.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Data
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartDto {
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String resolution;

    private String aggregate;

    private List<ParameterDataDto> parameterDatas = new ArrayList<>();

    private List<ParameterDto> parameterDtos = new ArrayList<>();
}
