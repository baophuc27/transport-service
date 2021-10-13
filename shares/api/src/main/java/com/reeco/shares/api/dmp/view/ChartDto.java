package com.reeco.shares.api.dmp.view;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class ChartDto {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private List<ParameterDataDto> parameterDatas = new ArrayList<>();

    private Long stationId;

    private List<ParameterDto> parameterDtos = new ArrayList<>();

}
