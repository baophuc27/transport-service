package com.reeco.transport.infrastructure.model.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reeco.transport.domain.DeviceConnection;

public abstract class ProtocolMessage {
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

    public DeviceConnection.NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(DeviceConnection.NotificationType notificationType) {
        this.notificationType = notificationType;
    }


}
