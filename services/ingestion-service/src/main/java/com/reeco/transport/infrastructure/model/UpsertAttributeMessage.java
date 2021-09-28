package com.reeco.transport.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpsertAttributeMessage {
    public static class Attribute{
        @JsonProperty("englishName")
        String englishName;

        @JsonProperty("vietnameseName")
        String vietnameseName;

        @JsonProperty("parameterType")
        String parameterType;

        @JsonProperty("indexType")
        String indexType;

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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Attribute{" +
                    "englishName='" + englishName + '\'' +
                    ", vietnameseName='" + vietnameseName + '\'' +
                    ", parameterType='" + parameterType + '\'' +
                    ", indexType='" + indexType + '\'' +
                    ", keyName='" + keyName + '\'' +
                    ", displayType='" + displayType + '\'' +
                    ", unit='" + unit + '\'' +
                    ", format='" + format + '\'' +
                    ", id=" + id +
                    '}';
        }
    }
    @JsonProperty("stationId")
    int stationId;

    @JsonProperty("attribute")
    Attribute attribute;

    @JsonProperty("connectionId")
    int connectionId;

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    @Override
    public String toString() {
        return "UpsertAttributeMessage{" +
                "stationId=" + stationId +
                ", attribute=" + attribute +
                ", connectionId=" + connectionId +
                '}';
    }
}
