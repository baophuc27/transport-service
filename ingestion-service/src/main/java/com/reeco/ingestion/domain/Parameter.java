package com.reeco.ingestion.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Parameter {
    private Long organizationId;
    private Long workspaceId;
    private Long paramId;
    private Long indicatorId;
    private Long stationId;
    private String unit;
    private Long connectionId;
    private String paramName;
    private String indicatorName;
    private LocalDateTime updatedAt;
}
