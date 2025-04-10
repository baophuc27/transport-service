package com.reeco.ingestion.application.mapper;

import com.reeco.common.model.enumtype.AlarmType;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumericalStatByOrg;
import com.reeco.ingestion.domain.NumericalStatEvent;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface NumStatEventMapper extends DomainEntityMapper<NumericalStatEvent, NumericalStatByOrg> {
    @Mappings({
            @Mapping(source = "partitionKey.organizationId", target = "organizationId"),
            @Mapping(source = "partitionKey.date", target = "date"),
            @Mapping(source = "partitionKey.paramId", target = "paramId"),
            @Mapping(source = "min", target = "min"),
            @Mapping(source = "max", target = "max"),
            @Mapping(source = "mean", target = "mean"),
            @Mapping(source = "acc", target = "acc"),
            @Mapping(source = "median", target = "median"),
            @Mapping(source = "count", target = "count"),
            @Mapping(source = "std", target = "std"),
            @Mapping(source = "isAlarm", target = "isAlarm"),
            @Mapping(source = "alarmId", target = "alarmId"),
            @Mapping(source = "alarmType", target = "alarmType"),
            @Mapping(source = "minValue", target = "minValue"),
            @Mapping(source = "maxValue", target = "maxValue"),
            @Mapping(source = "lastUpdated", target = "lastUpdated")

    })
    NumericalStatEvent toDomain(NumericalStatByOrg eventPort);

    @Mappings({
            @Mapping(source = "organizationId", target = "partitionKey.organizationId"),
            @Mapping(source = "date",target = "partitionKey.date"),
            @Mapping(source = "paramId", target = "partitionKey.paramId"),
            @Mapping(source = "min", target = "min"),
            @Mapping(source = "max", target = "max"),
            @Mapping(source = "mean", target = "mean"),
            @Mapping(source = "acc", target = "acc"),
            @Mapping(source = "count", target = "count"),
            @Mapping(source = "median", target = "median"),
            @Mapping(source = "std", target = "std"),
            @Mapping(source = "isAlarm", target = "isAlarm"),
            @Mapping(source = "alarmId", target = "alarmId"),
            @Mapping(source = "alarmType", target = "alarmType"),
            @Mapping(source = "minValue", target = "minValue"),
            @Mapping(source = "maxValue", target = "maxValue"),
            @Mapping(source = "lastUpdated", target = "lastUpdated")
    })
    NumericalStatByOrg toPersistence(NumericalStatEvent eventEntity);
}