package com.reeco.ingestion.application.mapper;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.NumericalStatByOrg;
import com.reeco.ingestion.domain.NumericalStatEvent;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.WARN,unmappedSourcePolicy = ReportingPolicy.IGNORE)
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
            @Mapping(source = "lastUpdated", target = "lastUpdated")
    })
    NumericalStatByOrg toPort(NumericalStatEvent eventEntity);
}
