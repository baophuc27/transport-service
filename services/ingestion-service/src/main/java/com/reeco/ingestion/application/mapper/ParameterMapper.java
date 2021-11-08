package com.reeco.ingestion.application.mapper;

import com.reeco.ingestion.domain.Parameter;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.ParamsByOrg;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.WARN,unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ParameterMapper extends DomainEntityMapper<Parameter, ParamsByOrg> {

    @Mappings({
            @Mapping(source = "partitionKey.organizationId", target = "organizationId"),
            @Mapping(source = "partitionKey.paramId", target = "paramId"),
            @Mapping(source = "indicatorId", target = "indicatorId"),
            @Mapping(source = "indicatorName", target = "indicatorName"),
            @Mapping(source = "paramName", target = "paramName"),
            @Mapping(source = "connectionId", target = "connectionId"),
            @Mapping(source = "stationId", target = "stationId"),
            @Mapping(source = "updatedAt", target = "updatedAt"),
            @Mapping(source = "unit", target = "unit"),

    })
    Parameter toDomain(ParamsByOrg eventPort);

}
