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

    public String getId() {
        return id;
    }

    public String getFormat() {
        return format;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Integer getLimit() {
        return limit;
    }

    public String getAggregate() {
        return aggregate;
    }

    public String getAggregateInterval() {
        return aggregateInterval;
    }
}
