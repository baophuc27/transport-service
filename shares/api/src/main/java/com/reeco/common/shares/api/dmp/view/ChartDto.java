package com.reeco.common.shares.api.dmp.view;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

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
