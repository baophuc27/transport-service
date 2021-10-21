package com.reeco.ingestion.application.mapper;

import com.reeco.ingestion.domain.CategoricalTsEvent;
import com.reeco.ingestion.domain.NumericalTsEvent;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.CategoricalTsByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumericalTsByOrg;
import org.mapstruct.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.WARN,unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface TsEventMapper {
    @Mappings({
            @Mapping(source = "organizationId", target = "partitionKey.organizationId"),
            @Mapping(source = "eventTime",target = "partitionKey.date", qualifiedBy = GetEventDate.class),
            @Mapping(source = "eventTime", target = "partitionKey.eventTime"),
            @Mapping(source = "paramId", target = "partitionKey.paramId"),
            @Mapping(source = "indicatorName", target = "indicatorName"),
            @Mapping(source = "connectionId", target = "connectionId"),
            @Mapping(source = "receivedAt", target = "receivedAt"),
            @Mapping(source = "value", target = "value"),
            @Mapping(source = "lat", target = "lat"),
            @Mapping(source = "lon", target = "lon")
    })
    NumericalTsByOrg toPort(NumericalTsEvent eventEntity);

    @Mappings({
            @Mapping(source = "organizationId", target = "partitionKey.organizationId"),
            @Mapping(source = "eventTime",target = "partitionKey.date", qualifiedBy = GetEventDate.class),
            @Mapping(source = "eventTime", target = "partitionKey.eventTime"),
            @Mapping(source = "paramId", target = "partitionKey.paramId"),
            @Mapping(source = "value", target = "partitionKey.value"),
            @Mapping(source = "indicatorName", target = "indicatorName"),
            @Mapping(source = "connectionId", target = "connectionId"),
            @Mapping(source = "receivedAt", target = "receivedAt"),
            @Mapping(source = "lat", target = "lat"),
            @Mapping(source = "lon", target = "lon")
    })
    CategoricalTsByOrg toPort(CategoricalTsEvent eventEntity);

    @GetEventDate
    default LocalDate getEventDate(LocalDateTime dateTime){
        LocalDate date = dateTime.toLocalDate();
        return date;
    }
}

@Qualifier
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
@interface GetEventDate{}