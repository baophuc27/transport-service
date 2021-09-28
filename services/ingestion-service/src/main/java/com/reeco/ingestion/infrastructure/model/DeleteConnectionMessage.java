package com.reeco.ingestion.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.reeco.ingestion.infrastructure.kafka.ActionType;
import com.reeco.ingestion.infrastructure.kafka.EntityType;

public class DeleteConnectionMessage {
    public static class ConnectionConfig {

        @JsonProperty("id")
        String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "ConnectionConfig{" +
                    "id='" + id + '\'' +
                    '}';
        }
    }

    @JsonProperty("stationId")
    String stationId;

    @JsonProperty("connection")
    ConnectionConfig connection;

    @JsonProperty("createdTime")
    long createdTime;

    @JsonProperty("actionType")
    ActionType eventAction;

    @JsonProperty("entityType")
    EntityType entityType;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public ActionType getEventAction() {
        return eventAction;
    }

    public void setEventAction(ActionType eventAction) {
        this.eventAction = eventAction;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public ConnectionConfig getConnection() {
        return connection;
    }

    public void setConnection(ConnectionConfig connection) {
        this.connection = connection;
    }

    @Override
    public String toString() {
        return "DeleteConnectionMessage{" +
                "stationId='" + stationId + '\'' +
                ", connection=" + connection +
                ", createdTime=" + createdTime +
                ", eventAction=" + eventAction +
                ", entityType=" + entityType +
                '}';
    }
}
