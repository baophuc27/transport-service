package com.reeco.transport.infrastructure.persistence.postgresql;

import javax.persistence.*;

@Entity
@Table(name = "attribute")
public class AttributeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "vietnamese_name")
    private String vietnameseName;

    @Column(name = "english_name")
    private String englishName;

    @Column(name = "parameter_type")
    private String parameterType;

    @Column(name = "key_name")
    private String keyName;

    @Column(name = "display_type")
    private String displayType;

    @Column(name = "unit")
    private String unit;

    @Column(name = "format")
    private String format;

    @Column(name = "device_id")
    private int deviceId;

   @Column(name = "organization_id")
    private int organizationId;

   @Column(name = "param_id")
   private int paramId;

   @Column(name = "station_id")
    private int stationId;

   @Column(name = "workspace_id")
    private int workspaceId;

   @Column(name = "indicator_id")
    private int indicatorId;

    @Column(name = "source_param_name")
    private String sourceParamName;

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

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }


    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
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

    public int getIndicatorId() {
        return indicatorId;
    }

    public void setIndicatorId(int indicatorId) {
        this.indicatorId = indicatorId;
    }

    public int getParamId() {
        return paramId;
    }

    public void setParamId(int paramId) {
        this.paramId = paramId;
    }

    public String getSourceParamName() {
        return sourceParamName;
    }

    public void setSourceParamName(String sourceParamName) {
        this.sourceParamName = sourceParamName;
    }

    public AttributeEntity() {
    }

    @Override
    public String toString() {
        return "AttributeEntity{" +
                "id=" + id +
                ", vietnameseName='" + vietnameseName + '\'' +
                ", englishName='" + englishName + '\'' +
                ", parameterType='" + parameterType + '\'' +
                ", keyName='" + keyName + '\'' +
                ", displayType='" + displayType + '\'' +
                ", unit='" + unit + '\'' +
                ", format='" + format + '\'' +
                ", deviceId=" + deviceId +
                ", organizationId=" + organizationId +
                ", paramId=" + paramId +
                ", stationId=" + stationId +
                ", workspaceId=" + workspaceId +
                ", indicatorId=" + indicatorId +
                ", sourceParamName=" + sourceParamName +
                '}';
    }
}
