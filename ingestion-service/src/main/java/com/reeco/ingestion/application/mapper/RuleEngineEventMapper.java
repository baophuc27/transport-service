package com.reeco.ingestion.application.mapper;

import com.reeco.common.model.enumtype.AlarmType;
import com.reeco.ingestion.application.port.in.RuleEngineEvent;
import com.reeco.ingestion.domain.NumericalStatEvent;
import com.reeco.ingestion.domain.NumericalTsEvent;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.CategoricalTsByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumericalStatByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumericalTsByOrg;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)

public interface RuleEngineEventMapper{
    @Mappings({
            @Mapping(source = "organizationId", target = "partitionKey.organizationId"),
            @Mapping(source = "stationId", target = "stationId"),
            @Mapping(source = "connectionId", target = "connectionId"),
            @Mapping(source = "paramId", target = "partitionKey.paramId"),
            @Mapping(source = "eventTime", target = "partitionKey.eventTime"),
            @Mapping(source = "indicatorId", target = "indicatorId"),
            @Mapping(source = "indicatorName", target = "indicatorName"),
            @Mapping(source = "workspaceId", target = "workspaceId"),
            @Mapping(source = "paramName", target = "paramName"),
            @Mapping(source = "receivedAt", target = "receivedAt"),
            @Mapping(source = "sentAt", target = "sentAt"),
            @Mapping(source = "lat", target = "lat"),
            @Mapping(source = "lon", target = "lon"),
            @Mapping(source = "isAlarm", target = "isAlarm"),
            @Mapping(source = "alarmId", target = "alarmId"),
            @Mapping(source = "alarmType", target = "alarmType"),
            @Mapping(source = "minValue", target = "minValue"),
            @Mapping(source = "maxValue", target = "maxValue"),
            @Mapping(source = "eventTime",target = "date", qualifiedBy = GetEventDate.class),
    })
    NumericalTsByOrg toNumPersistence(RuleEngineEvent eventEntity);


    @Mappings({
            @Mapping(source = "organizationId", target = "partitionKey.organizationId"),
            @Mapping(source = "stationId", target = "stationId"),
            @Mapping(source = "connectionId", target = "connectionId"),
            @Mapping(source = "paramId", target = "partitionKey.paramId"),
            @Mapping(source = "eventTime", target = "partitionKey.eventTime"),
            @Mapping(source = "indicatorId", target = "indicatorId"),
            @Mapping(source = "workspaceId", target = "workspaceId"),
            @Mapping(source = "indicatorName", target = "indicatorName"),
            @Mapping(source = "paramName", target = "paramName"),
            @Mapping(source = "value", target = "value"),
            @Mapping(source = "receivedAt", target = "receivedAt"),
            @Mapping(source = "sentAt", target = "sentAt"),
            @Mapping(source = "lat", target = "lat"),
            @Mapping(source = "lon", target = "lon"),
            @Mapping(source = "isAlarm", target = "isAlarm"),
            @Mapping(source = "alarmId", target = "alarmId"),
            @Mapping(source = "alarmType", target = "alarmType"),
            @Mapping(source = "minValue", target = "minValue"),
            @Mapping(source = "maxValue", target = "maxValue"),
            @Mapping(source = "eventTime",target = "date", qualifiedBy = GetEventDate.class),
    })
    CategoricalTsByOrg toCatPersistence(RuleEngineEvent eventEntity);

    @GetEventDate
    default LocalDate getEventDate(LocalDateTime dateTime){
        LocalDate date = dateTime.toLocalDate();
        return date;
    }
}

