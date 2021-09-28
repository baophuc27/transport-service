package com.reeco.ingestion.domain.protocol;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class SFTPConfiguration extends ProtocolConfiguration {
    private String hostAddress;
    private String hostPort;
    private String key;
    private String password;
    private String folderName;
}
