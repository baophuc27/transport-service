package com.reeco.transport.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpsertCustomIdMessage {

    @JsonProperty("customId")
    String customId;

    @JsonProperty("customIdType")
    String customIdType;

    @JsonProperty("originalId")
    String originalId;

    public String getCustomId() {
        return customId;
    }

    public String getCustomIdType() {
        return customIdType;
    }

    public String getOriginalId() {
        return originalId;
    }

    @Override
    public String toString() {
        return "UpsertCustomIdMessage{" +
                "customId='" + customId + '\'' +
                ", customIdType='" + customIdType + '\'' +
                ", originalId='" + originalId + '\'' +
                '}';
    }
}
