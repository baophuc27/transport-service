package com.reeco.ingestion.application.mapper;

import com.reeco.ingestion.domain.CategoricalTsEvent;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.CategoricalTsByOrg;
import org.mapstruct.*;

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
            @Mapping(source = "eventTime", target = "partitionKey.eventTime"),
            @Mapping(source = "paramId", target = "partitionKey.paramId"),
            @Mapping(source = "value", target = "partitionKey.value"),
            @Mapping(source = "indicatorName", target = "indicatorName"),
            @Mapping(source = "eventTime",target = "date", qualifiedBy = GetEventDate.class),
            @Mapping(source = "connectionId", target = "connectionId"),
            @Mapping(source = "receivedAt", target = "receivedAt"),
            @Mapping(source = "lat", target = "lat"),
            @Mapping(source = "lon", target = "lon")
    })
    CategoricalTsByOrg toPersistence(CategoricalTsEvent eventEntity);

    @GetEventDate
    default LocalDate getEventDate(LocalDateTime dateTime){
        LocalDate date = dateTime.toLocalDate();
        return date;
    }
}

