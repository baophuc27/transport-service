package com.reeco.transport.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class APIKeyDeviceInfoDTO
{
    String _id;

    Boolean isActive;

    LocalDateTime lastActiveTime;

    public APIKeyDeviceInfoDTO(String _id, Boolean isActive, LocalDateTime lastActiveTime, String name, String customId, Boolean isConnecting) {
        this._id = _id;
        this.isActive = isActive;
        this.lastActiveTime = lastActiveTime;
        this.name = name;
        this.customId = customId;
        this.isConnecting = isConnecting;
    }

    String name;

    String customId;

    Boolean isConnecting;

    @Override
    public String toString() {
        return "APIKeyDeviceInfoDTO{" +
                "_id='" + _id + '\'' +
                ", isActive=" + isActive +
                ", lastActiveTime=" + lastActiveTime +
                ", name='" + name + '\'' +
                ", customId='" + customId + '\'' +
                ", isConnecting=" + isConnecting +
                '}';
    }
}
