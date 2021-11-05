package com.reeco.ingestion.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Parameter {
    private Long organizationId;
    private Long paramId;
    private Long indicatorId;
    private Long stationId;
    private String unit;
    private Long connectionId;
    private String paramName;
    private String indicatorName;
    private LocalDateTime updatedAt;

    @AllArgsConstructor
    @Getter
    public static class ParamsByOrg{
        private Long organizationId;
        private List<Long> paramsId;
    }
}
