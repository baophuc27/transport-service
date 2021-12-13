package com.reeco.ingestion.application.mapper;

import com.reeco.ingestion.domain.CategoricalStatEvent;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.CategoricalStatByOrg;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.WARN,unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CatStatEventMapper extends DomainEntityMapper<CategoricalStatEvent, CategoricalStatByOrg> {
    @Mappings({
            @Mapping(source = "partitionKey.organizationId", target = "organizationId"),
            @Mapping(source = "partitionKey.paramId", target = "paramId"),
            @Mapping(source = "partitionKey.date", target = "date"),
            @Mapping(source = "partitionKey.value", target = "value"),
            @Mapping(source = "valueCount", target = "valueCount"),
            @Mapping(source = "lastUpdated", target = "lastUpdated")

    })
    CategoricalStatEvent toDomain(CategoricalStatByOrg eventPort);

    @Mappings({
            @Mapping(source = "organizationId", target = "partitionKey.organizationId"),
            @Mapping(source = "paramId", target = "partitionKey.paramId"),
            @Mapping(source = "date",target = "partitionKey.date"),
            @Mapping(source = "value", target = "partitionKey.value"),
            @Mapping(source = "valueCount", target = "valueCount"),
            @Mapping(source = "lastUpdated", target = "lastUpdated")
    })
    CategoricalStatByOrg toPersistence(CategoricalStatEvent eventEntity);
}
