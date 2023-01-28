package com.reeco.transport.infrastructure.persistence.postgresql;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="api_key")
public class ApiKeyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="permission")
    private String permission;

    @Column(name = "key")
    private String key;

    @Column(name = "organizationids")
    private String organizationIds;

    @Column(name = "parameterids")
    private String parameterIds;

    @Column(name = "connectionids")
    private String connectionIds;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getConnectionIds() {
        return connectionIds;
    }

    public void setConnectionIds(String connectionIds) {
        this.connectionIds = connectionIds;
    }

    public ApiKeyEntity() {
    }

    @Override
    public String toString() {
        return "ApiKeyEntity{" +
                "id=" + id +
                ", permission='" + permission + '\'' +
                ", key='" + key + '\'' +
                ", organizationIds='" + organizationIds + '\'' +
                ", parameterIds='" + parameterIds + '\'' +
                ", connectionIds='" + connectionIds + '\'' +
                '}';
    }
}
