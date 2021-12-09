package com.reeco.http.model.entity;

import com.reeco.common.model.enumtype.AlarmType;
import com.reeco.common.model.enumtype.TransportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;

@Table("device_connection_by_organization")
@AllArgsConstructor
@Data
public class ConnectionByOrg {
    @PrimaryKeyClass
    @AllArgsConstructor
    @Data
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "organization_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long organizationId;

        @PrimaryKeyColumn(name = "connection_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
        private Long connectionId;

    }

    @PrimaryKey
    private ConnectionByOrg.Key partitionKey;

    @Column("access_token")
    private String accessToken;

    @Column("name")
    private String name;

    @Column("transport_type")
    @CassandraType(type = CassandraType.Name.TEXT)
    private TransportType transportType;
}
