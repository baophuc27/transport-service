package com.reeco.transport.infrastructure.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteApiKeyMessage {

    @JsonProperty("id")
    Integer id;

    @JsonProperty("permission")
    String permission;

    @JsonProperty("key")
    String key;

    @JsonProperty("scope")
    DataScopeMessage dataScope;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public DataScopeMessage getDataScope() {
        return dataScope;
    }

    public void setDataScope(DataScopeMessage dataScope) {
        this.dataScope = dataScope;
    }

    public DeleteApiKeyMessage() {
    }

    @Override
    public String toString() {
        return "UpsertApiKeyMessage{" +
                "id=" + id +
                ", permission='" + permission + '\'' +
                ", key='" + key + '\'' +
                ", dataScope=" + dataScope +
                '}';
    }
}
