package com.reeco.transport.domain;

import lombok.Data;

@Data
public class GetHistoricDataDTO {
    String id;

    String format;

    String startTime;

    String endTime;

    Integer limit;

    String aggregate;

    String aggregateInterval;
}
