package com.reeco.transport.infrastructure.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DataScopeMessage {

    @JsonProperty("organizationIds")
    List<Integer> organizationIds;

    @JsonProperty("parameterIds")
    List<Integer> parameterIds;

    @JsonProperty("connectionIds")
    List<Integer> connectionIds;

    public List<Integer> getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(List<Integer> organizationIds) {
        this.organizationIds = organizationIds;
    }

    public List<Integer> getParameterIds() {
        return parameterIds;
    }

    public void setParameterIds(List<Integer> parameterIds) {
        this.parameterIds = parameterIds;
    }

    public List<Integer> getConnectionIds() {
        return connectionIds;
    }

    public void setConnectionIds(List<Integer> connectionIds) {
        this.connectionIds = connectionIds;
    }

    public DataScopeMessage() {
    }

    @Override
    public String toString() {
        return "DataScopeMessage{" +
                "organizationIds=" + organizationIds +
                ", parameterIds=" + parameterIds +
                ", connectionIds=" + connectionIds +
                '}';
    }
}
