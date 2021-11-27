package com.reeco.ingestion.infrastructure.persistence.cassandra.entity;

import com.reeco.common.model.enumtype.AlarmType;
import com.reeco.common.model.enumtype.FrequenceType;
import com.reeco.common.model.enumtype.MaintainType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table("alarm")
@AllArgsConstructor
@Data
public class AlarmInfo {
    @PrimaryKeyClass
    @AllArgsConstructor
    @Getter
    public static class Key implements Serializable {

        @PrimaryKeyColumn(name = "organization_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
        private Long organizationId;

        @PrimaryKeyColumn(name = "param_id", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
        private Long paramId;

        @PrimaryKeyColumn(name = "alarm_id", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
        private Long alarmId;
    }

    @PrimaryKey
    private AlarmInfo.Key partitionKey;

    @Column("alarm_type")
    @CassandraType(type = CassandraType.Name.TEXT)
    private AlarmType alarmType;

    @Column("min_value")
    private String minValue;

    @Column("max_value")
    private String maxValue;

    @Column("num_of_match")
    private Long numOfMatch;

    @Column("maintain_type")
    @CassandraType(type = CassandraType.Name.TEXT)
    private MaintainType maintainType;

    @Column("frequence")
    private Long frequence;

    @Column("frequence_type")
    @CassandraType(type = CassandraType.Name.TEXT)
    private FrequenceType frequenceType;

    @Column("updated_at")
    private LocalDateTime updatedAt;

}
