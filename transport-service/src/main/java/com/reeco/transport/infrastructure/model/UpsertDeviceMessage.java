package com.reeco.transport.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.reeco.transport.domain.DeviceConnection;
import com.reeco.transport.infrastructure.model.protocol.ProtocolMessage;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpsertDeviceMessage extends ProtocolMessage {

    @JsonProperty("deviceId")
    int deviceId;

    @JsonProperty("organizationId")
    int organizationId;

    @JsonProperty("workspaceId")
    int workspaceId;

    @JsonProperty("stationId")
    int stationId;

    @JsonProperty("id")
    String id;

    @JsonProperty("fileType")
    String fileType;

    @JsonProperty("templateFormat")
    int templateFormat;

    @JsonProperty("maximumTimeout")
    int maximumTimeout;

    @JsonProperty("maximumAttachment")
    int maximumAttachment;

    @JsonProperty("notificationType")
    DeviceConnection.NotificationType notificationType;

    @JsonProperty("protocol")
    String protocol;

    @JsonProperty("vietnameseName")
    String vietnameseName;

    @JsonProperty("englishName")
    String englishName;

    @JsonProperty("active")
    Boolean active;

    public int getStationId() { return stationId; }

    public void setStationId(int stationId) { this.stationId = stationId; }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int connectionId) {
        this.deviceId = connectionId;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getTemplateFormat() {
        return templateFormat;
    }

    public void setTemplateFormat(int templateFormat) {
        this.templateFormat = templateFormat;
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

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "FTPMessage{" +
                ", vietnameseName='" + vietnameseName + '\'' +
                ", englishName='" + englishName + '\'' +
                ", id='" + id + '\'' +
                ", fileType='" + fileType + '\'' +
                ", templateFormat=" + templateFormat +
                ", maximumTimeout=" + maximumTimeout +
                ", maximumAttachment=" + maximumAttachment +
                ", notificationType='" + notificationType + '\'' +
                '}';
    }
}
