package com.reeco.ingestion.application.mapper;

import com.reeco.ingestion.application.port.in.IncomingTsEvent;
import com.reeco.ingestion.domain.NumericTsEvent;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumTsByStation;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TsEventMapper {
    @Mappings({
            @Mapping(source = "deviceId", target = "connection_id"),
            @Mapping(source = "stationId", target = "station_id"),
            @Mapping(source = "key", target = "parameter"),
            @Mapping(source = "value", target = "measure"),
            @Mapping(source = "timeStamp", target = "event_ts", qualifiedBy = FormatLocalDateTime.class),
            @Mapping(source = "receivedAt", target = "received_at", qualifiedBy = FormatLocalDateTime.class),
            @Mapping(expression = "java(getNowTime())", target = "sent_at"),
            @Mapping(source = "timeStamp",target = "date", qualifiedBy = GetLocalDate.class),
            @Mapping(source = "lat", target = "lat"),
            @Mapping(source = "lon", target = "lon")
    })
    NumTsByStation mapDomainToCassEntity(NumericTsEvent numericTsEvent);

    @Mappings({
            @Mapping(source = "deviceId", target = "connection_id"),
            @Mapping(source = "stationId", target = "station_id"),
            @Mapping(source = "key", target = "parameter"),
            @Mapping(source = "value", target = "measure"),
            @Mapping(source = "timeStamp", target = "event_ts", qualifiedBy = FormatLocalDateTime.class),
            @Mapping(source = "receivedAt", target = "received_at", qualifiedBy = FormatLocalDateTime.class),
            @Mapping(expression = "java(getNowTime())", target = "sent_at"),
            @Mapping(source = "timeStamp",target = "date", qualifiedBy = GetLocalDate.class),
            @Mapping(source = "lat", target = "lat"),
            @Mapping(source = "lon", target = "lon")
    })
    NumericTsEvent mapEventToDomain(IncomingTsEvent incomingTsEvent);

    @FormatLocalDateTime
    default String format(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    default String getNowTime(){
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return time.format(formatter);
    }

    @GetLocalDate
    default String getNowDate(LocalDateTime dateTime){
        LocalDate date = dateTime.toLocalDate();
        return date.toString();
    }

    default Double convertStringToDouble(String s){
        try{
            return Double.valueOf(s);}
        catch (NumberFormatException e) {
            return null;
        }
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