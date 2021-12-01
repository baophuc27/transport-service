package com.reeco.ingestion.application.mapper;


import com.reeco.common.model.dto.Parameter;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.ParamsByOrg;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ParameterMapper extends DomainEntityMapper<Parameter, ParamsByOrg> {

    @Mappings({
            @Mapping(source = "partitionKey.organizationId", target = "organizationId"),
            @Mapping(source = "partitionKey.paramId", target = "id"),
            @Mapping(source = "indicatorId", target = "indicatorId"),
            @Mapping(source = "paramName", target = "keyName"),
            @Mapping(source = "workspaceId", target = "workspaceId"),
            @Mapping(source = "connectionId", target = "connectionId"),
            @Mapping(source = "stationId", target = "stationId"),
            @Mapping(source = "englishName", target = "englishName"),
            @Mapping(source = "vietnameseName", target = "vietnameseName"),
            @Mapping(source = "parameterType", target = "parameterType"),
            @Mapping(source = "displayType", target = "displayType"),
            @Mapping(source = "format", target = "format"),
            @Mapping(source = "unit", target = "unit")
    })
    Parameter toDomain(ParamsByOrg paramsByOrg);

    @Mappings({
            @Mapping(source = "organizationId", target = "partitionKey.organizationId"),
            @Mapping(source = "id", target = "partitionKey.paramId"),
            @Mapping(source = "indicatorId", target = "indicatorId"),
            @Mapping(source = "keyName", target = "paramName"),
            @Mapping(source = "connectionId", target = "connectionId"),
            @Mapping(source = "stationId", target = "stationId"),
            @Mapping(source = "englishName", target = "englishName"),
            @Mapping(source = "vietnameseName", target = "vietnameseName"),
            @Mapping(source = "parameterType", target = "parameterType"),
            @Mapping(source = "displayType", target = "displayType"),
            @Mapping(source = "format", target = "format"),
            @Mapping(source = "unit", target = "unit")
    })
    ParamsByOrg toPersistence(Parameter parameter);

}
