package com.reeco.core.dmp.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
//import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ChartDto {
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String resolution;

    private List<ParameterDataDto> parameterDatas = new ArrayList<>();

    // private Long stationId;

    private List<ParameterDto> parameterDtos = new ArrayList<>();
}
