package com.reeco.transport.application.mapper;

import com.reeco.transport.infrastructure.model.UpsertCustomIdMessage;
import com.reeco.transport.infrastructure.persistence.postgresql.CustomIdEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",uses = {},unmappedTargetPolicy = ReportingPolicy.WARN,unmappedSourcePolicy = ReportingPolicy.WARN)
public interface CustomIdMapper {

    @Mappings({
            @Mapping(source = "customId",target = "customId"),
            @Mapping(source = "customIdType",target = "customIdType"),
            @Mapping(source = "originalId",target = "originalId")
    })
    CustomIdEntity messageToEntity(UpsertCustomIdMessage upsertCustomIdMessage);
}
