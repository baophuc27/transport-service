package com.reeco.ingestion.application.mapper;


import com.reeco.common.model.dto.HTTPConnection;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.ConnectionInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ConnectionMapper extends DomainEntityMapper<HTTPConnection, ConnectionInfo> {
    @Mappings({
            @Mapping(source = "organizationId", target = "partitionKey.organizationId"),
            @Mapping(source = "id", target = "partitionKey.connectionId"),
            @Mapping(source = "workspaceId", target = "workspaceId"),
            @Mapping(source = "accessToken", target = "accessToken"),
            @Mapping(source = "stationId", target = "stationId"),
            @Mapping(source = "englishName", target = "name"),
            @Mapping(source = "vietnameseName", target = "nameVi"),
            @Mapping(source = "transportType", target = "transportType"),
            @Mapping(source = "updatedAt", target = "updatedAt")
    })
    ConnectionInfo toPersistence(HTTPConnection httpConnection);
}
