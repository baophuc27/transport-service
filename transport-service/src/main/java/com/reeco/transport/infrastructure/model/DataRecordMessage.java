package com.reeco.transport.infrastructure.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
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

    String indicatorName;
}
