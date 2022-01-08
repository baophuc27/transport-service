package com.reeco.transport.application.mapper;

import com.reeco.transport.application.usecase.DeleteDeviceCommand;
import com.reeco.transport.application.usecase.RegisterDeviceCommand;
import com.reeco.transport.domain.DeviceConnection;
import com.reeco.transport.infrastructure.model.UpsertConnectionMessage;
import com.reeco.transport.infrastructure.model.DeleteConnectionMessage;
import com.reeco.transport.infrastructure.persistence.postgresql.DeviceEntity;
import com.reeco.transport.infrastructure.persistence.postgresql.FtpEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import com.reeco.common.model.dto.FTPConnection;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE,unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ConnectionMapper {
    @Mappings({
            @Mapping(source = "organizationId", target = "stationId"),
            @Mapping(source = "workspaceId",target = "workspaceId"),
            @Mapping(source = "vietnameseName", target = "vietnameseName"),
            @Mapping(source = "englishName",target = "englishName"),
            @Mapping(source = "port",  target = "protocolConfiguration.hostPort"),
            @Mapping(source= "userName", target = "protocolConfiguration.userName"),
            @Mapping(source = "hostName", target = "protocolConfiguration.hostAddress"),
            @Mapping(source = "password", target = "protocolConfiguration.password"),
            @Mapping(source = "key", target = "protocolConfiguration.key"),
            @Mapping(source = "maximumTimeout", target = "protocolConfiguration.maximumTimeoutSeconds"),
            @Mapping(source = "ipWhiteList", target = "ipWhiteList"),
            @Mapping(source = "fileType", target = "protocolConfiguration.fileType"),
            @Mapping(source = "id",target = "deviceId"),
            @Mapping(source = "maximumAttachment", target = "maximumAttachment"),
            @Mapping(source = "removeAfterDays",target = "removeAfterDays"),
            @Mapping(source = "notificationType", target = "notificationType"),
            @Mapping(source = "templateFormat", target = "protocolConfiguration.templateFormat"),
            @Mapping(source = "active", target = "active")
    })
    RegisterDeviceCommand messageToRegisterCommand(FTPConnection message);

    @Mappings({
            @Mapping(source = "deviceId", target = "deviceId"),
            @Mapping(source = "stationId",target = "stationId"),
            @Mapping(source = "workspaceId",target = "workspaceId"),
            @Mapping(source = "vietnameseName", target = "vietnameseName"),
            @Mapping(source = "englishName", target = "englishName"),
            @Mapping(source = "ipWhiteList", target = "ipWhiteList"),
            @Mapping(source = "maximumAttachment", target = "maximumAttachment"),
            @Mapping(source = "removeAfterDays", target = "removeAfterDays"),
            @Mapping(source = "notificationType", target = "notificationType"),
            @Mapping(source = "protocolConfiguration.fileType", target = "protocolConfiguration.fileType" ),
            @Mapping(source = "protocolConfiguration.hostPort", target = "protocolConfiguration.hostPort"),
            @Mapping(source = "protocolConfiguration.userName", target= "protocolConfiguration.userName"),
            @Mapping(source = "protocolConfiguration.hostAddress",target = "protocolConfiguration.hostAddress"),
            @Mapping(source = "protocolConfiguration.password", target = "protocolConfiguration.password"),
            @Mapping(source = "protocolConfiguration.key", target = "protocolConfiguration.key"),
            @Mapping(source = "protocolConfiguration.maximumTimeoutSeconds", target = "protocolConfiguration.maximumTimeoutSeconds"),
            @Mapping(source = "protocolConfiguration.templateFormat",target = "protocolConfiguration.templateFormat"),
            @Mapping(source = "active", target = "active")
    })
    DeviceConnection registerCommandToFTPDeviceConnection(RegisterDeviceCommand command);

    @Mappings({
            @Mapping(source = "connection.id", target = "deviceId")
    })
    DeleteDeviceCommand messageToDeleteCommand(DeleteConnectionMessage message);

    @Mappings({
            @Mapping(source = "deviceId",target = "id"),
            @Mapping(source = "vietnameseName",target = "vietnameseName"),
            @Mapping(source = "englishName", target = "englishName"),
            @Mapping(source = "protocolConfiguration.maximumTimeoutSeconds", target = "maximumTimeout"),
            @Mapping(source = "maximumAttachment", target = "maximumAttachment"),
            @Mapping(source = "protocolConfiguration.key", target = "protocolType"),
            @Mapping(source = "protocolConfiguration.templateFormat", target = "templateFormat"),
            @Mapping(source = "stationId", target = "stationId"),
            @Mapping(source = "workspaceId",target = "workspaceId"),
            @Mapping(source = "active",target = "active")
    })
    DeviceEntity domainToDeviceEntity(DeviceConnection deviceConnection);

    @Mappings({
            @Mapping(source = "deviceId", target = "id"),
            @Mapping(source = "protocolConfiguration.userName", target = "userName"),
            @Mapping(source = "protocolConfiguration.password", target = "password"),
            @Mapping(source = "protocolConfiguration.key", target = "key"),
            @Mapping(source = "protocolConfiguration.hostAddress", target = "hostName"),
            @Mapping(source = "protocolConfiguration.hostPort", target = "hostPort"),
            @Mapping(source = "protocolConfiguration.fileType", target = "fileType")
    })
    FtpEntity domainToFtpEntity(DeviceConnection deviceConnection);
}
