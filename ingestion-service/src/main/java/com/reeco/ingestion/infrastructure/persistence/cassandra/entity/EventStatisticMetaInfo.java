package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;

import com.reeco.common.model.enumtype.ParamType;
import com.reeco.common.model.enumtype.ValueType;
import lombok.*;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table("event_statistics_meta_info")
@AllArgsConstructor
@Getter
@ToString
public class EventStatisticMetaInfo {

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
    private EventStatisticMetaInfo.Key partitionKey;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("last_agg_time")
    @Setter
    private LocalDateTime lastAggTime;

}
