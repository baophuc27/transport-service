package com.reeco.shares.api.dmp.view;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class ChartDto {

    private Timestamp startTime;

    private Timestamp endTime;

    private List<IndicatorDataDto> indicatorDatas = new ArrayList<>();

    private Long stationId;

    private List<IndicatorDto> indicatorDtos = new ArrayList<>();

}
