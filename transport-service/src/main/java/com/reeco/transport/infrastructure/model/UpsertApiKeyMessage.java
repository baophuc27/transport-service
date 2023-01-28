package com.reeco.transport.infrastructure.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpsertApiKeyMessage {

    @JsonProperty("id")
    Integer id;

    @JsonProperty("permission")
    String permission;

    @JsonProperty("apiKey")
    String apiKey;

    @JsonProperty("scope")
    DataScopeMessage scope;

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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public DataScopeMessage getScope() {
        return scope;
    }

    public void setScope(DataScopeMessage scope) {
        this.scope = scope;
    }

    public UpsertApiKeyMessage() {
    }

    @Override
    public String toString() {
        return "UpsertApiKeyMessage{" +
                "id=" + id +
                ", permission='" + permission + '\'' +
                ", key='" + apiKey + '\'' +
                ", dataScope=" + scope +
                '}';
    }
}
