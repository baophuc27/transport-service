package com.reeco.transport.infrastructure.persistence.postgresql;

import javax.persistence.*;

@Entity
@Table(name = "attribute")
public class AttributeEntity {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "vietnamese_name")
    private String vietnameseName;

    @Column(name = "english_name")
    private String englishName;

    @Column(name = "parameter_type")
    private String parameterType;

    @Column(name = "index_type")
    private String indexType;

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

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
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

    public AttributeEntity() {
    }

    @Override
    public String toString() {
        return "AttributeEntity{" +
                "id=" + id +
                ", vietnameseName='" + vietnameseName + '\'' +
                ", englishName='" + englishName + '\'' +
                ", parameterType='" + parameterType + '\'' +
                ", indexType='" + indexType + '\'' +
                ", keyName='" + keyName + '\'' +
                ", displayType='" + displayType + '\'' +
                ", unit='" + unit + '\'' +
                ", format='" + format + '\'' +
                ", deviceId=" + deviceId +
                '}';
    }
}
