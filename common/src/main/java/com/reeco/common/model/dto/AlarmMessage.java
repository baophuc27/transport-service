package com.reeco.common.model.dto;

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

    String ipAddress;

    String lastEventTime;

    String sentAt;

    public AlarmMessage() {
    }


    @Override
    public String toString() {
        return "AlarmMessage{" +
                "organizationId=" + organizationId +
                ", connectionId=" + connectionId +
                ", workspaceId=" + workspaceId +
                ", stationId=" + stationId +
                ", message='" + message + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", lastEventTime='" + lastEventTime + '\'' +
                ", sentAt='" + sentAt + '\'' +
                '}';
    }
}
