package com.reeco.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reeco.model.Protocol;
import lombok.Getter;

import java.util.List;


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FTPConnectionDTO extends BaseConnectionDTO {

    private static final long serialVersionUID = 1L;

    private String userName;

    private String hostName;

    private String port;

    private String password;

    private Boolean useSSL;

    private String key;

    private List<String> ipWhiteList;

    private String fileType;

    private Integer templateFormat;

    private Integer maximumTimeout;

    private Integer maximumAttachment;

    private String notificationType;

    private Long removeAfterDays;

    public FTPConnectionDTO() {
        super(-1L);
    }

    public FTPConnectionDTO(Long id) {
        super(id);
    }

    public FTPConnectionDTO(Long id, String englishName, String vietnameseName, Protocol protocol, String userName, String hostName, String port, String password, Boolean useSSL, String key, List<String> ipWhiteList, String fileType, Integer templateFormat, Integer maximumTimeout, Integer maximumAttachment, String notificationType, Long removeAfterDays) {
        super(id, englishName, vietnameseName, protocol);
        this.userName = userName;
        this.hostName = hostName;
        this.port = port;
        this.password = password;
        this.useSSL = useSSL;
        this.key = key;
        this.ipWhiteList = ipWhiteList;
        this.fileType = fileType;
        this.templateFormat = templateFormat;
        this.maximumTimeout = maximumTimeout;
        this.maximumAttachment = maximumAttachment;
        this.notificationType = notificationType;
        this.removeAfterDays = removeAfterDays;
    }
}
