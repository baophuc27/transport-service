package com.reeco.common.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.reeco.common.framework.EnumNamePattern;
import com.reeco.common.model.enumtype.Protocol;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
public class BaseConnection implements Connection {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "connectionId must be not NULL")
    private Long id;

    @NotNull(message = "organizationId must be not NULL")
    private Long organizationId;

    @NotNull(message = "workspaceId must be not NULL")
    private Long workspaceId;

    @NotNull(message = "englishName must be not NULL")
    @NotBlank(message = "englishName must be not BLANK")
    private String englishName;

    @NotNull(message = "vietnameseName must be not NULL")
    @NotBlank(message = "vietnameseName must be not BLANK")
    private String vietnameseName;

    @NotNull(message = "active must be not NULL")
    private Boolean active;

    @EnumNamePattern(regexp = "FTP|FTPS|HTTP", message = "protocol must be in {FTP, FTPS, HTTP}")
    private Protocol protocol;


    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime receivedAt;

    public BaseConnection() {
    }

    public BaseConnection(Long id,
                          Long organizationId,
                          Long workspaceId,
                          String englishName,
                          String vietnameseName,
                          Boolean active,
                          Protocol protocol) {

        this.id = id;
        this.organizationId = organizationId;
        this.englishName = englishName;
        this.vietnameseName = vietnameseName;
        this.workspaceId = workspaceId;
        this.active = active;
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return "BaseConnection{" +
                "id=" + id +
                ", organizationId=" + organizationId +
                ", workspaceId=" + workspaceId +
                ", englishName='" + englishName + '\'' +
                ", vietnameseName='" + vietnameseName + '\'' +
                ", active=" + active +
                ", protocol=" + protocol +
                ", receivedAt='" + receivedAt + '\'' +
                '}';
    }
}
