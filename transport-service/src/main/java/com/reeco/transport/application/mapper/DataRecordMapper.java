package com.reeco.transport.application.mapper;

import com.reeco.transport.domain.DataRecord;
import com.reeco.transport.infrastructure.model.DataRecordMessage;
import com.reeco.transport.infrastructure.persistence.postgresql.AttributeEntity;
import org.mapstruct.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DataRecordMapper {
    @Mappings({
            @Mapping(source = "data.deviceId", target = "connectionId"),
            @Mapping(source = "data.key", target = "paramName"),
            @Mapping(source = "data.value", target = "value"),
            @Mapping(source = "data.timeStamp", target = "eventTime", qualifiedBy = FormatLocalDateTime.class),
            @Mapping(source = "data.receivedAt", target = "receivedAt", qualifiedBy = FormatLocalDateTime.class),
            @Mapping(expression = "java(getNowTime())", target = "sentAt"),
            @Mapping(source = "data.lat", target = "lat"),
            @Mapping(source = "data.lon", target = "lon"),
            @Mapping(source = "attribute.organizationId", target = "organizationId"),
            @Mapping(source = "attribute.stationId", target = "stationId"),
            @Mapping(source = "attribute.indicatorId", target = "indicatorId"),
            @Mapping(source = "attribute.workspaceId", target = "workspaceId"),
            @Mapping(source = "attribute.paramId",target = "paramId")
    })
    DataRecordMessage domainEntityToMessage(DataRecord data, AttributeEntity attribute);

    @FormatLocalDateTime
    default String format(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return dateTime.format(formatter);
    }

    default String getNowTime(){
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return time.format(formatter);
    }

    @GetLocalDate
    default String getNowDate(LocalDateTime dateTime){
        LocalDate date = dateTime.toLocalDate();
        return date.toString();
    }
}

@Qualifier
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
@interface FormatLocalDateTime{
}

@Qualifier
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
@interface GetLocalDate{}