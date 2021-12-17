package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;


import com.reeco.common.model.enumtype.AlarmType;
import com.reeco.common.model.enumtype.TransportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table("device_connection_by_organization")
@AllArgsConstructor
@Data
public class ConnectionInfo {

    @PrimaryKeyClass
    @AllArgsConstructor
    @ToString
    @Data
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "organization_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long organizationId;

        @PrimaryKeyColumn(name = "connection_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Long connectionId;

    }

    @PrimaryKey
    private ConnectionInfo.Key partitionKey;

    @Column("transport_type")
    @CassandraType(type = CassandraType.Name.TEXT)
    private TransportType transportType;

    @Column("access_token")
    private String accessToken;

    @Column("name")
    private String name;

    @Column("name_vi")
    private String nameVi;

    @Column("station_id")
    private Long stationId;

    @Column("workspace_id")
    private Long workspaceId;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("expired_time")
    private LocalDateTime expiredTime;

}
