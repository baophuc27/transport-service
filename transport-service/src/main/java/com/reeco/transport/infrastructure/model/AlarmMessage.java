package com.reeco.transport.infrastructure.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class AlarmMessage {

    Integer organizationId;

    Integer connectionId;

    Integer workspaceId;

    Integer stationId;

    String message;

    String lastEventTime;

    String sentAt;
}
