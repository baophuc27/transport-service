package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table("indicators")
@AllArgsConstructor
@Getter
public class IndicatorInfo {


    @PrimaryKeyClass
    @AllArgsConstructor
    @Getter
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "indicator_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long indicatorId;

    }

    @PrimaryKey
    private IndicatorInfo.Key partitionKey;

    @Column("group_id")
    private Long groupId;

    @Column("indicator_name")
    private String indicatorName;

    @Column("indicator_name_vi")
    private String indicatorNameVi;

    @Column("value_type")
    private String valueType;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("standard_unit")
    private String standardUnit;
}
