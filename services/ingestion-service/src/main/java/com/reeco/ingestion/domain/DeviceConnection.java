package com.reeco.ingestion.domain;

import java.util.List;

import com.reeco.ingestion.domain.protocol.FTPConfiguration;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
public final class DeviceConnection {

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

//    private DeviceConnection(Builder builder){
//        this.deviceId = builder.deviceId;
//        this.protocolConfiguration = builder.protocolConfiguration;
//        this.maximumTimeoutMinutes = builder.maximumTimeoutMinutes;
//        this.maximumAttachment = builder.maximumAttachment;
//        this.removeAfterDays = builder.removeAfterDays;
//        this.ipWhiteList = builder.ipWhiteList;
//        this.vietnameseName = builder.vietnameseName;
//        this.englishName = builder.englishName;
//        this.notificationType = builder.notificationType;
//    }

//    public static class Builder {
//        private final String deviceId;
//
//        private final FTPConfiguration protocolConfiguration;
//
//        private int maximumTimeoutMinutes;
//
//        private int maximumAttachment;
//
//        private int removeAfterDays;
//
//        private NotificationType notificationType;
//
//        private List<String> ipWhiteList;
//
//        private String vietnameseName;
//
//        private String englishName;
//
//        public Builder(String deviceId, FTPConfiguration protocolConfiguration){
//            if (deviceId == null || protocolConfiguration == null){
//                throw new InvalidInputException("Device ID and protocol configuration must not be null");
//            }
//            this.deviceId = deviceId;
//            this.protocolConfiguration = protocolConfiguration;
//        }
//
//        public Builder withMaximumTimeoutMinutes(int maximumTimeoutMinutes){
//            this.maximumTimeoutMinutes = maximumTimeoutMinutes;
//            return this;
//        }
//
//        public Builder withMaximumAttachment(int maximumAttachment){
//            this.maximumAttachment = maximumAttachment;
//            return this;
//        }
//
//        public Builder withRemoveAfterDays(int removeAfterDays){
//            this.removeAfterDays = removeAfterDays;
//            return this;
//        }
//
//        public Builder withNotificationType(NotificationType notificationType){
//            this.notificationType = notificationType;
//            return this;
//        }
//
//        public Builder withIpWhiteList(List<String> ipWhiteList){
//            this.ipWhiteList = ipWhiteList;
//            return this;
//        }
//
//        public Builder withVietnameseName(String vietnameseName){
//            this.vietnameseName = vietnameseName;
//            return this;
//        }
//
//        public Builder withEnglishName(String englishName){
//            this.englishName = englishName;
//            return this;
//        }
//
//        public DeviceConnection build(){
//            return new DeviceConnection(this);
//        }
//    }
}