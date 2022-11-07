package com.reeco.ingestion.application.mapper;

import com.reeco.common.model.dto.Alarm;
import com.reeco.common.model.dto.AlarmMessage;
import com.reeco.common.model.dto.Connection;
import com.reeco.common.model.enumtype.Protocol;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.AlarmInfo;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.ConnectionAlarmInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring",
        uses = {},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AlarmMapper extends DomainEntityMapper<Alarm, AlarmInfo> {
    @Mappings({
            @Mapping(source = "partitionKey.organizationId", target = "organizationId"),
            @Mapping(source = "partitionKey.paramId", target = "paramId"),
            @Mapping(source = "partitionKey.alarmId", target = "id"),
            @Mapping(source = "alarmType", target = "alarmType"),
            @Mapping(source = "minValue", target = "minValue"),
            @Mapping(source = "maxValue", target = "maxValue"),
            @Mapping(source = "numOfMatch", target = "numOfMatch"),
            @Mapping(source = "maintainType", target = "maintainType"),
            @Mapping(source = "frequence", target = "frequence"),
            @Mapping(source = "frequenceType", target = "frequenceType")

    })
    Alarm toDomain(AlarmInfo alarmInfo);

    @Mappings({
            @Mapping(source = "organizationId", target = "partitionKey.organizationId"),
            @Mapping(source = "paramId", target = "partitionKey.paramId"),
            @Mapping(source = "id", target = "partitionKey.alarmId"),
            @Mapping(source = "alarmType", target = "alarmType"),
            @Mapping(source = "minValue", target = "minValue"),
            @Mapping(source = "maxValue", target = "maxValue"),
            @Mapping(source = "numOfMatch", target = "numOfMatch"),
            @Mapping(source = "maintainType", target = "maintainType"),
            @Mapping(source = "frequence", target = "frequence"),
            @Mapping(source = "frequenceType", target = "frequenceType")
    })
    AlarmInfo toPersistence(Alarm alarm);

    @Mappings({
            @Mapping(source = "alarmMessage.organizationId", target = "partitionKey.organizationId"),
            @Mapping(source = "alarmMessage.connectionId", target = "partitionKey.connectionId"),
            @Mapping(source = "alarmMessage.message",target = "alarmType"),
            @Mapping(source = "protocol",target = "connectionProtocol"),
            @Mapping(expression = "java(getNowTime())",target = "alarmTime"),
            @Mapping(source = "description", target = "description")
    })
    ConnectionAlarmInfo fromAlarmMessage(AlarmMessage alarmMessage, String description, Protocol protocol);

    default LocalDateTime getNowTime(){
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        return time.format(formatter);
        return time;
    }

}
