package com.reeco.core.dmp.core.model.cassandra;


import com.reeco.common.model.enumtype.Protocol;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Table("connection_alarm")
@AllArgsConstructor
@Data
public class ConnectionAlarmInfo {

    @PrimaryKeyClass
    @AllArgsConstructor
    @Data
    public static class ConnectionAlarmKey implements Serializable {

        @PrimaryKeyColumn(name="organization_id", ordinal = 0, type= PrimaryKeyType.PARTITIONED)
        private Long organizationId;

        @PrimaryKeyColumn(name = "connection_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
        private Long connectionId;

        @Override
        public String toString() {
            return "ConnectionAlarmKey{" +
                    "organizationId=" + organizationId +
                    ", connectionId=" + connectionId +
                    '}';
        }
    }

    @PrimaryKey
    private ConnectionAlarmKey partitionKey;

    @Column("alarm_time")
    private LocalDateTime alarmTime;

    @CassandraType(type=CassandraType.Name.TEXT)
    @Column("alarm_type")
    private String alarmType;

    @Column("connection_protocol")
    private Protocol connectionProtocol;

    @Column("description")
    private String description;

    @Column("is_deleted")
    private Boolean isDeleted;

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
