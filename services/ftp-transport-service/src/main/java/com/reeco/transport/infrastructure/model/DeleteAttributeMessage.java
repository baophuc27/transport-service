package com.reeco.transport.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteAttributeMessage {
    public static class Attribute{
        @JsonProperty("id")
        int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Attribute{" +
                    "id=" + id +
                    '}';
        }
    }
    @JsonProperty("stationId")
    int stationId;

    @JsonProperty("attribute")
    Attribute attribute;

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

    @Override
    public String toString() {
        return "DeleteAttributeMessage{" +
                "stationId=" + stationId +
                ", attribute=" + attribute +
                '}';
    }
}
