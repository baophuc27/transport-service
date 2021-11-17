package com.reeco.core.dmp.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table("indicators")
@AllArgsConstructor
@Getter
@Data
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

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("group_id")
    private Long groupId;

    @Column("indicator_name_vi")
    private String indicatorNameVi;

    @Column("indicator_name")
    private String indicatorName;

    @Column("value_type")
    private String valueType;

    @Column("standard_unit")
    private String standardUnit;

    @Column("updated_at")
    private LocalDateTime updatedAt;
}
