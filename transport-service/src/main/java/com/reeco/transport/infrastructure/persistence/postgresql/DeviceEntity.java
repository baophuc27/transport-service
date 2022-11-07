package com.reeco.transport.infrastructure.persistence.postgresql;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "device")
@Data
public class DeviceEntity {
    @Id
    private int id;

    @Column(name = "organization_id")
    private int stationId;

    @Column(name = "workspace_id")
    private int workspaceId;

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

    @Column(name = "active")
    private Boolean active;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Column(name = "last_active",columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    @UpdateTimestamp
    private LocalDateTime lastActive;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(int workspaceId) {
        this.workspaceId = workspaceId;
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

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public int getTemplateFormat() {
        return templateFormat;
    }

    public void setTemplateFormat(int templateFormat) {
        this.templateFormat = templateFormat;
    }

    public DeviceEntity(int id, int stationId, int workspaceId, String vietnameseName, String englishName, int maximumTimeout, int maximumAttachment, String notificationType, String protocolType, int templateFormat) {
        this.id = id;
        this.stationId = stationId;
        this.workspaceId = workspaceId;
        this.vietnameseName = vietnameseName;
        this.englishName = englishName;
        this.maximumTimeout = maximumTimeout;
        this.maximumAttachment = maximumAttachment;
        this.notificationType = notificationType;
        this.protocolType = protocolType;
        this.templateFormat = templateFormat;
    }

    public DeviceEntity() {
        this.active= true;
    }

    @Override
    public String toString() {
        return "DeviceEntity{" +
                "id=" + id +
                ", stationId=" + stationId +
                ", workspaceId=" + workspaceId +
                ", vietnameseName='" + vietnameseName + '\'' +
                ", englishName='" + englishName + '\'' +
                ", maximumTimeout=" + maximumTimeout +
                ", maximumAttachment=" + maximumAttachment +
                ", notificationType='" + notificationType + '\'' +
                ", protocolType='" + protocolType + '\'' +
                ", templateFormat=" + templateFormat +
                ", active=" + active +
                ", lastActive=" + lastActive +
                '}';
    }
}

