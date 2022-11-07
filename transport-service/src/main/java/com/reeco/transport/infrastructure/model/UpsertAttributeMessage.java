package com.reeco.transport.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpsertAttributeMessage {

    @JsonProperty("englishName")
    String englishName;

    @JsonProperty("vietnameseName")
    String vietnameseName;

    @JsonProperty("parameterType")
    String parameterType;

    @JsonProperty("keyName")
    String keyName;

    @JsonProperty("displayType")
    String displayType;

    @JsonProperty("unit")
    String unit;

    @JsonProperty("format")
    String format;

    @JsonProperty("id")
    int id;

    @JsonProperty("stationId")
    int stationId;

    @JsonProperty("connectionId")
    int connectionId;

    @JsonProperty("organizationId")
    int organizationId;

    @JsonProperty("workspaceId")
    int workspaceId;

    @JsonProperty("indicatorId")
    int indicatorId;

//    @JsonProperty("sourceParamName")
//    String sourceParamName;
//
    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getVietnameseName() {
        return vietnameseName;
    }

    public void setVietnameseName(String vietnameseName) {
        this.vietnameseName = vietnameseName;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }


    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

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

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
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

    public int getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(int indicatorId) {
        this.indicatorId = indicatorId;
    }

//    public String getSourceParamName() {
//        return sourceParamName;
//    }
//
//    public void setSourceParamName(String sourceParamName) {
//        this.sourceParamName = sourceParamName;
//    }

    @Override
    public String toString() {
        return "UpsertAttributeMessage{" +
                "englishName='" + englishName + '\'' +
                ", vietnameseName='" + vietnameseName + '\'' +
                ", parameterType='" + parameterType + '\'' +
                ", keyName='" + keyName + '\'' +
                ", displayType='" + displayType + '\'' +
                ", unit='" + unit + '\'' +
                ", format='" + format + '\'' +
                ", id=" + id +
                ", stationId=" + stationId +
                ", connectionId=" + connectionId +
                ", organizationId=" + organizationId +
                ", workspaceId=" + workspaceId +
                ", indicatorId=" + indicatorId +
//                ", sourceParamName=" + sourceParamName +
                '}';
    }
}
