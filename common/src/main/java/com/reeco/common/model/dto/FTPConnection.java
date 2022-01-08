package com.reeco.common.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reeco.common.model.enumtype.Protocol;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;


@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class FTPConnection extends BaseConnection {

    private static final long serialVersionUID = 1L;

    private String userName;

    @NotNull(message = "hostName must be not NULL")
    @NotBlank(message = "hostName must be not BLANK")
    private String hostName;

    @NotNull(message = "port must be not NULL")
    @NotBlank(message = "port must be not BLANK")
    private String port;

    private String password;

    private Boolean useSSL;

    private String key;

    private List<String> ipWhiteList;

    private String fileType;

    @NotNull(message = "templateFormat must be not NULL")
    private Integer templateFormat;

    private Integer maximumTimeout;

    private Integer maximumAttachment;

    private String notificationType;

    private Long removeAfterDays;

    public FTPConnection() {
    }

    public FTPConnection(Long id, Long organizationId, Long stationId,  Long workspaceId, String englishName, String vietnameseName, Boolean active, Protocol protocol, String userName, @NotNull(message = "hostName must be not NULL") @NotBlank(message = "hostName must be not BLANK") String hostName, @NotNull(message = "port must be not NULL") @NotBlank(message = "port must be not BLANK") String port, String password, Boolean useSSL, String key, List<String> ipWhiteList, String fileType, @NotNull(message = "templateFormat must be not NULL") Integer templateFormat, Integer maximumTimeout, Integer maximumAttachment, String notificationType, Long removeAfterDays) {
        super(id, organizationId, stationId, workspaceId, englishName, vietnameseName, active, protocol);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FTPConnection)) return false;
        if (!super.equals(o)) return false;
        FTPConnection that = (FTPConnection) o;
        return Objects.equals(getUserName(), that.getUserName()) &&
                Objects.equals(getHostName(), that.getHostName()) &&
                Objects.equals(getPort(), that.getPort()) &&
                Objects.equals(getPassword(), that.getPassword()) &&
                Objects.equals(getUseSSL(), that.getUseSSL()) &&
                Objects.equals(getKey(), that.getKey()) &&
                Objects.equals(getIpWhiteList(), that.getIpWhiteList()) &&
                Objects.equals(getFileType(), that.getFileType()) &&
                Objects.equals(getTemplateFormat(), that.getTemplateFormat()) &&
                Objects.equals(getMaximumTimeout(), that.getMaximumTimeout()) &&
                Objects.equals(getMaximumAttachment(), that.getMaximumAttachment()) &&
                Objects.equals(getNotificationType(), that.getNotificationType()) &&
                Objects.equals(getRemoveAfterDays(), that.getRemoveAfterDays());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getUserName(), getHostName(), getPort(), getPassword(), getUseSSL(), getKey(), getIpWhiteList(), getFileType(), getTemplateFormat(), getMaximumTimeout(), getMaximumAttachment(), getNotificationType(), getRemoveAfterDays(),getActive());
    }
}
