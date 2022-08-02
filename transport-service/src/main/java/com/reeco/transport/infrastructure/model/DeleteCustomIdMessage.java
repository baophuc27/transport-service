package com.reeco.transport.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteCustomIdMessage {

    @JsonProperty("customIdType")
    public
    String customIdType;

    @JsonProperty("originalId")
    public
    Integer originalId;

    @Override
    public String toString() {
        return "DeleteCustomIdMessage{" +
                "customIdType='" + customIdType + '\'' +
                ", originalId=" + originalId +
                '}';
    }
}
