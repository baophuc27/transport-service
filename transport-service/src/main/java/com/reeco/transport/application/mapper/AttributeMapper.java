package com.reeco.transport.application.mapper;


import com.reeco.transport.infrastructure.model.UpsertAttributeMessage;
import com.reeco.transport.infrastructure.persistence.postgresql.AttributeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE,unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AttributeMapper {
    @Mappings({
            @Mapping(source = "englishName", target = "englishName"),
            @Mapping(source = "vietnameseName", target = "vietnameseName"),
            @Mapping(source = "parameterType", target = "parameterType"),
            @Mapping(source = "displayType", target = "displayType"),
            @Mapping(source = "unit", target = "unit"),
            @Mapping(source = "format", target = "format"),
            @Mapping(source = "id", target = "paramId"),
            @Mapping(source = "keyName", target = "keyName"),
            @Mapping(source = "connectionId", target = "deviceId"),
            @Mapping(source = "stationId", target = "stationId"),
            @Mapping(source = "organizationId", target = "organizationId"),
            @Mapping(source = "workspaceId",target = "workspaceId"),
            @Mapping(source = "indicatorId", target = "indicatorId"),
            @Mapping(source = "sourceParamName", target = "sourceParamName"),
    })
    AttributeEntity messageToAttributeEntity(UpsertAttributeMessage upsertAttributeMessage);

}
