package com.reeco.common.model.dto;

import com.reeco.common.framework.EnumNamePattern;
import com.reeco.common.model.enumtype.Protocol;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Getter
public class BaseConnection implements Serializable, Connection {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "connectionId must be not NULL")
    private Long id;

    @NotNull(message = "organizationId must be not NULL")
    private Long organizationId;

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

    @Getter
    @Setter
    private Long receivedAt;

    public BaseConnection(@NotNull(message = "connectionId must be not NULL") Long id) {
        this.id = id;
    }

    public BaseConnection(Long id,
                          Long organizationId,
                          String englishName,
                          String vietnameseName,
                          Boolean active,
                          Protocol protocol) {

        this.id = id;
        this.organizationId = organizationId;
        this.englishName = englishName;
        this.vietnameseName = vietnameseName;
        this.active = active;
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return "BaseConnection{" +
                "id=" + id +
                ", organizationId=" + organizationId +
                ", englishName='" + englishName + '\'' +
                ", vietnameseName='" + vietnameseName + '\'' +
                ", active=" + active +
                ", protocol=" + protocol +
                '}';
    }
}
