package com.reeco.ingestion.domain;

import java.util.List;

import com.reeco.ingestion.domain.protocol.FTPConfiguration;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
public final class Device {

    public enum FileType{
        TXT,CSV,XML
    }

    public enum NotificationType{
        OUTDATED_OR_UPDATED, OUTDATED, UPDATED, NEVER
    }

    private int stationId;

    private String deviceId;

    private FTPConfiguration protocolConfiguration;

    private int maximumAttachment;

    private int removeAfterDays;

    private String notificationType;

    private List<String> ipWhiteList;

    private String vietnameseName;

    private String englishName;

}