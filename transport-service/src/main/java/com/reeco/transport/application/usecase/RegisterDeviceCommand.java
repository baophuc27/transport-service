package com.reeco.transport.application.usecase;

import com.reeco.transport.domain.DeviceConnection;
import com.reeco.transport.domain.protocol.FTPConfiguration;
import com.reeco.transport.utils.annotators.SelfValidating;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class RegisterDeviceCommand extends SelfValidating<RegisterDeviceCommand> {
    @NotNull int stationId;

    @NotNull int deviceId;

    @NotNull int workspaceId;

    @NotNull
    @Positive
    int maximumAttachment;

    @NotNull
    int removeAfterDays;

    @NotNull
    String notificationType;

    @NotNull
    List<String> ipWhiteList;

    @NotNull FTPConfiguration protocolConfiguration;

    @NotNull
    String vietnameseName;

    @NotNull
    String englishName;

    public RegisterDeviceCommand(int stationId, int deviceId, int workspaceId, int maximumAttachment, int removeAfterDays, String notificationType, List<String> ipWhiteList, FTPConfiguration protocolConfiguration, String vietnameseName, String englishName) {
        this.stationId = stationId;
        this.deviceId = deviceId;
        this.workspaceId = workspaceId;
        this.maximumAttachment = maximumAttachment;
        this.removeAfterDays = removeAfterDays;
        this.notificationType = notificationType;
        this.ipWhiteList = ipWhiteList;
        this.protocolConfiguration = protocolConfiguration;
        this.vietnameseName = vietnameseName;
        this.englishName = englishName;
    }
}
