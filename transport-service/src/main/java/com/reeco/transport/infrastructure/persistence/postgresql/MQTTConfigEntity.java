package com.reeco.transport.infrastructure.persistence.postgresql;
import javax.persistence.*;
@Entity
@Table(name="mqtt_acls")
public class MQTTConfigEntity {

    @Id
    @Column(name="id")
    private String id;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="topic")
    private String topic;

    @Column(name="acc")
    private int access;

    @Column(name="connectionids")
    private String connectionIds;

    @Column(name = "organizationids")
    private String organizationIds;

    @Column(name = "parameterids")
    private String parameterIds;

    @Column(name="is_active")
    private boolean isActive;

    @Column(name="use_ssl")
    private boolean useSSL;

    @Column(name="name")
    private String name;

    public MQTTConfigEntity(String id, String username, String password, String topic, int access, String connectionIds, String organizationIds, String parameterIds, boolean isActive, boolean useSSL, String name){
        this.id = id;
        this.username = username;
        this.password = password;
        this.topic = topic;
        this.access = access;
        this.connectionIds = connectionIds;
        this.organizationIds = organizationIds;
        this.parameterIds = parameterIds;
        this.isActive = isActive;
        this.useSSL = useSSL;
        this.name = name;
    }

    public MQTTConfigEntity() {
        this.isActive = true;
        this.access = 5;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getAccess() {
        return access;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public String getConnectionIds() {
        return connectionIds;
    }

    public void setConnectionIds(String connectionIds) {
        this.connectionIds = connectionIds;
    }

    public String getOrganizationIds() {
        return organizationIds;
    }

    public void setOrganizationIds(String organizationIds) {
        this.organizationIds = organizationIds;
    }

    public String getParameterIds() {
        return parameterIds;
    }

    public void setParameterIds(String parameterIds) {
        this.parameterIds = parameterIds;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    @Override
    public String toString() {
        return "MQTTConfigEntity{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", topic='" + topic + '\'' +
                ", access=" + access +
                ", connectionIds='" + connectionIds + '\'' +
                ", organizationIds='" + organizationIds + '\'' +
                ", parameterIds='" + parameterIds + '\'' +
                ", isActive=" + isActive +
                ", useSSL=" + useSSL +
                '}';
    }
}
