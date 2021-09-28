package com.reeco.ingestion.domain.protocol;

import com.reeco.ingestion.domain.DeviceConnection;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class FTPConfiguration extends ProtocolConfiguration {
    private String hostAddress;
    private String hostPort;
    private String key;
    private String password;
    private String userName;
    private DeviceConnection.FileType fileType;
    private int maximumTimeoutSeconds;
    private int templateFormat;
}
