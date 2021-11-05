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
public interface CatEventMapper extends DomainEntityMapper<CategoricalTsEvent, CategoricalTsByOrg> {
    @Mappings({
            @Mapping(source = "partitionKey.organizationId", target = "organizationId"),
            @Mapping(source = "partitionKey.eventTime", target = "eventTime"),
            @Mapping(source = "partitionKey.paramId", target = "paramId"),
            @Mapping(source = "partitionKey.value", target = "value"),
            @Mapping(source = "indicatorName", target = "indicatorName"),
            @Mapping(source = "connectionId", target = "connectionId"),
            @Mapping(source = "receivedAt", target = "receivedAt"),
            @Mapping(source = "lat", target = "lat"),
            @Mapping(source = "lon", target = "lon")
    })
    CategoricalTsEvent toDomain(CategoricalTsByOrg eventPort);

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

