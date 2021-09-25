package com.reeco.transport.application.mapper;


import com.reeco.transport.infrastructure.model.UpsertAttributeMessage;
import com.reeco.transport.infrastructure.persistence.postgresql.AttributeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.WARN,unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AttributeMapper {
    @Mappings({
//            @Mapping(source = "stationId", target = "deviceId"),
            @Mapping(source = "attribute.englishName", target = "englishName"),
            @Mapping(source = "attribute.vietnameseName", target = "vietnameseName"),
            @Mapping(source = "attribute.parameterType", target = "parameterType"),
            @Mapping(source = "attribute.indexType", target = "indexType"),
            @Mapping(source = "attribute.displayType", target = "displayType"),
            @Mapping(source = "attribute.unit", target = "unit"),
            @Mapping(source = "attribute.format", target = "format"),
            @Mapping(source = "attribute.id", target = "id"),
            @Mapping(source = "attribute.keyName", target = "keyName"),
            @Mapping(source = "connectionId", target = "deviceId")
    })
    AttributeEntity messageToAttributeEntity(UpsertAttributeMessage upsertAttributeMessage);

}
