package com.reeco.transport.application.mapper;

import com.reeco.transport.infrastructure.model.AlarmMessage;
import com.reeco.transport.infrastructure.persistence.postgresql.DeviceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.ERROR, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AlarmMapper {
//    TODO: do not have organization
    @Mappings({
            @Mapping(source = "device.workspaceId", target = "workspaceId"),
            @Mapping(source = "device.id", target = "connectionId"),
            @Mapping(source = "device.stationId", target = "stationId"),
            @Mapping(source = "device.stationId", target = "organizationId"),
            @Mapping(source = "message", target = "message"),
            @Mapping(source = "eventTime", target = "lastEventTime"),
            @Mapping(expression = "java(getNowTime())", target = "sentAt")
    })
    AlarmMessage fromDeviceEntity(DeviceEntity device, String message, String eventTime);

    default String getNowTime(){
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }
}


