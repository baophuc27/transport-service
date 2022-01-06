package com.reeco.transport.infrastructure.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class DataRecordMessage {

    Integer organizationId;

    Integer workspaceId;

    Integer stationId;

    Integer connectionId;

    Integer indicatorId;

    Integer paramId;

    String eventTime;

    String receivedAt;

    String sentAt;

    String paramName;

    Double value;

    Double lat;

    Double lon;

}
