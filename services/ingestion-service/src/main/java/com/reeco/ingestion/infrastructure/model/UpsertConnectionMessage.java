package com.reeco.ingestion.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.reeco.ingestion.infrastructure.kafka.ActionType;
import com.reeco.ingestion.infrastructure.kafka.EntityType;
import com.reeco.ingestion.infrastructure.model.protocol.FTPMessage;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpsertConnectionMessage {

    @JsonProperty("stationId")
    int stationId;

    @JsonProperty("connection")
    FTPMessage connection;

    @JsonProperty("createdTime")
    long createdTime;

    @JsonProperty("actionType")
    ActionType eventAction;

    @JsonProperty("entityType")
    EntityType entityType;

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public void setConnection(FTPMessage connection) {
        this.connection = connection;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public void setEventAction(ActionType eventAction) {
        this.eventAction = eventAction;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public int getStationId() {
        return stationId;
    }

    public FTPMessage getConnection() {
        return connection;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public ActionType getEventAction() {
        return eventAction;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public String toString() {
        return "ConnectionMessage{" +
                "stationId='" + stationId + '\'' +
                ", connection=" + connection +
                ", createdTime=" + createdTime +
                ", eventAction=" + eventAction +
                ", entityType=" + entityType +
                '}';
    }
}
