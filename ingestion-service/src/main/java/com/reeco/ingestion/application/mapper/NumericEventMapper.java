package com.reeco.ingestion.application.mapper;

import com.reeco.ingestion.domain.NumericalTsEvent;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumericalTsByOrg;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface NumericEventMapper {

    @Mappings({
            @Mapping(source = "partitionKey.organizationId", target = "organizationId"),
            @Mapping(source = "partitionKey.eventTime", target = "eventTime"),
            @Mapping(source = "partitionKey.paramId", target = "paramId"),
            @Mapping(source = "workspaceId", target = "workspaceId"),
            @Mapping(source = "indicatorName", target = "indicatorName"),
            @Mapping(source = "connectionId", target = "connectionId"),
            @Mapping(source = "receivedAt", target = "receivedAt"),
            @Mapping(source = "value", target = "value"),
            @Mapping(source = "lat", target = "lat"),
            @Mapping(source = "lon", target = "lon")
    })
    NumericalTsEvent toDomain(NumericalTsByOrg eventPort);

    @Mappings({
            @Mapping(source = "organizationId", target = "partitionKey.organizationId"),
            @Mapping(source = "eventTime",target = "date", qualifiedBy = GetEventDate.class),
            @Mapping(source = "eventTime", target = "partitionKey.eventTime"),
            @Mapping(source = "paramId", target = "partitionKey.paramId"),
            @Mapping(source = "workspaceId", target = "workspaceId"),
            @Mapping(source = "indicatorName", target = "indicatorName"),
            @Mapping(source = "connectionId", target = "connectionId"),
            @Mapping(source = "receivedAt", target = "receivedAt"),
            @Mapping(source = "value", target = "value"),
            @Mapping(source = "lat", target = "lat"),
            @Mapping(source = "lon", target = "lon")
    })
    NumericalTsByOrg toPersistence(NumericalTsEvent eventEntity);


    @GetEventDate
    default LocalDate getEventDate(LocalDateTime dateTime){
        LocalDate date = dateTime.toLocalDate();
        return date;
    }
}

