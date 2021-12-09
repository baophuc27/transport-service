package com.reeco.http.model.entity;

//import com.reeco.common.model.enumtype.ParamType;
import com.reeco.common.model.enumtype.AlarmType;
import lombok.*;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;

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

//    @Column("indicator_name")
//    private String indicatorName;

    @Column("param_name")
    private String paramName;

    @Column("station_id")
    private Long stationId;

    @Column("connection_id")
    private Long connectionId;

    @Column("unit")
    private String unit;



//    @Column("updated_at")
//    private LocalDateTime updatedAt;

}