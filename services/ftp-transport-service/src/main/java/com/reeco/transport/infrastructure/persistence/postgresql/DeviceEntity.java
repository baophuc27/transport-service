package com.reeco.transport.infrastructure.persistence.postgresql;

import javax.persistence.*;

@Entity
@Table(name = "device")
public class DeviceEntity {
    @Id
    private int id;

    @Column(name = "station_id")
    private int stationId;


    @Column(name = "vietnamese_name")
    private String vietnameseName;

    @Column(name = "english_name")
    private String englishName;

    @Column(name = "maximum_timeout")
    private int maximumTimeout;

    @Column(name = "maximum_attachment")
    private int maximumAttachment;

    @Column(name = "notification_type")
    private String notificationType;

    @Column(name = "protocol_type")
    private String protocolType;

    @Column(name = "template_format")
    private int templateFormat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVietnameseName() {
        return vietnameseName;
    }

    public void setVietnameseName(String vietnameseName) {
        this.vietnameseName = vietnameseName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public int getMaximumTimeout() {
        return maximumTimeout;
    }

    public void setMaximumTimeout(int maximumTimeout) {
        this.maximumTimeout = maximumTimeout;
    }

    public int getMaximumAttachment() {
        return maximumAttachment;
    }

    public void setMaximumAttachment(int maximumAttachment) {
        this.maximumAttachment = maximumAttachment;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public int getTemplateFormat() {
        return templateFormat;
    }

    public void setTemplateFormat(int templateFormat) {
        this.templateFormat = templateFormat;
    }

    public DeviceEntity(int id, String vietnameseName, String englishName, int maximumTimeout, int maximumAttachment, String notificationType, String protocolType, int templateFormat) {
        this.id = id;
        this.vietnameseName = vietnameseName;
        this.englishName = englishName;
        this.maximumTimeout = maximumTimeout;
        this.maximumAttachment = maximumAttachment;
        this.notificationType = notificationType;
        this.protocolType = protocolType;
        this.templateFormat = templateFormat;
    }

    public DeviceEntity() {
    }

    @Override
    public String toString() {
        return "DeviceEntity{" +
                "id=" + id +
                ", stationId=" + stationId +
                ", vietnameseName='" + vietnameseName + '\'' +
                ", englishName='" + englishName + '\'' +
                ", maximumTimeout=" + maximumTimeout +
                ", maximumAttachment=" + maximumAttachment +
                ", notificationType='" + notificationType + '\'' +
                ", protocolType='" + protocolType + '\'' +
                ", templateFormat=" + templateFormat +
                '}';
    }
}

