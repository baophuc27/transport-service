package com.reeco.core.dmp.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table("params_by_organization")
@AllArgsConstructor
@Data
public class ParamsByOrg {
    @PrimaryKeyClass
    @AllArgsConstructor
    @Data
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "organization_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long organizationId;

        @PrimaryKeyColumn(name = "param_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
        private Long paramId;

    }

    @PrimaryKey
    private ParamsByOrg.Key partitionKey;

    @Column("indicator_id")
    private Long indicatorId;

    private String indicatorName;

    @Column("param_name")
    private String paramName;

    private Long stationId;

    private Long connectionId;

    @Column("updated_at")
    private LocalDateTime updatedAt;

}
