package com.reeco.transport.infrastructure.model;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpsertMQTTMessage {

    @JsonProperty("id")
    String id;

    @JsonProperty("name")
    String name;

    @JsonProperty("topicName")
    String topicName;

    @JsonProperty("destination")
    String destination;

    @JsonProperty("useSSL")
    Boolean useSSL;

    @JsonProperty("username")
    String username;

    @JsonProperty("password")
    String password;

    @JsonProperty("scope")
    DataScopeMessage scope;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Boolean getUseSSL() {
        return useSSL;
    }

    public void setUseSSL(Boolean useSSL) {
        this.useSSL = useSSL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DataScopeMessage getScope() {
        return scope;
    }

    public void setScope(DataScopeMessage scope) {
        this.scope = scope;
    }

    public UpsertMQTTMessage() {
    }

    @Override
    public String toString() {
        return "UpsertMQTTMessage{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", topicName='" + topicName + '\'' +
                ", destination='" + destination + '\'' +
                ", useSSL=" + useSSL +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", scope=" + scope +
                '}';
    }
}
