package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;

import com.reeco.common.framework.EnumNamePattern;
import com.reeco.common.model.enumtype.ParamType;
import lombok.*;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Table("params_by_organization")
@AllArgsConstructor
@Getter
@ToString
public class ParamsByOrg {

    @PrimaryKeyClass
    @AllArgsConstructor
    @Data
    @ToString
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "organization_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long organizationId;

        @PrimaryKeyColumn(name = "param_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Long paramId;
    }

    @PrimaryKey
    private ParamsByOrg.Key partitionKey;

    @Column("indicator_id")
    private Long indicatorId;

    @Column("workspace_id")
    private Long workspaceId;

    @Column("param_name")
    private String paramName;

    @Column("station_id")
    private Long stationId;

    @Column("connection_id")
    private Long connectionId;

    @Column("unit")
    private String unit;

    @Column("english_name")
    private String englishName;

    @Column("vietnamese_name")
    private String vietnameseName;

    @Column("parameter_type")
    @CassandraType(type = CassandraType.Name.TEXT)
    private ParamType parameterType;

    @Column("format")
    private String format;

    @Column("display_type")
    private String displayType;

//    @Column("updated_at")
//    private LocalDateTime updatedAt;

    @Column("last_agg_time")
    @Setter
    private LocalDateTime lastAggTime;
}
