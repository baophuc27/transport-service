package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;

@Table("indicators")
@AllArgsConstructor
@Getter
public class Indicator {


    @PrimaryKeyClass
    @AllArgsConstructor
    @Getter
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "indicator_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long indicatorId;

    }

    @PrimaryKey
    private Indicator.Key partitionKey;

    @Column("group_id")
    private Long groupId;

    @Column("group_name")
    private String groupName;

    @Column("indicator_name")
    private String indicatorName;

    @Column("value_type")
    private String valueType;

    @Column("unit")
    private String unit;

    @Column("standard_unit")
    private String standardUnit;
}
