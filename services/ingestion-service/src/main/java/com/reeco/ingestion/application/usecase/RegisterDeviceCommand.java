package com.reeco.ingestion.application.usecase;

import com.reeco.ingestion.domain.Device;
import com.reeco.ingestion.domain.protocol.FTPConfiguration;
import com.reeco.ingestion.utils.annotators.SelfValidating;
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

    @NotNull
    @Positive
    int maximumAttachment;

    @NotNull
    int removeAfterDays;

    @NotNull
    Device.NotificationType notificationType;

    @NotNull
    List<String> ipWhiteList;

    @NotNull FTPConfiguration protocolConfiguration;

    @NotNull
    String vietnameseName;

    @NotNull
    String englishName;

    public RegisterDeviceCommand(int stationId, int deviceId, Device.FileType fileType, int maximumAttachment, int removeAfterDays, Device.NotificationType notificationType, List<String> ipWhiteList, FTPConfiguration protocolConfiguration, String vietnameseName, String englishName) {
        this.stationId = stationId;
        this.deviceId = deviceId;
        this.maximumAttachment = maximumAttachment;
        this.removeAfterDays = removeAfterDays;
        this.notificationType = notificationType;
        this.ipWhiteList = ipWhiteList;
        this.protocolConfiguration = protocolConfiguration;
        this.vietnameseName = vietnameseName;
        this.englishName = englishName;
        this.validateSelf();
    }
}
