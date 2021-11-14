package com.reeco.model.dto;

import com.reeco.framework.EnumNamePattern;
import com.reeco.model.BaseEntity;
import com.reeco.model.Connection;
import com.reeco.model.Protocol;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@ToString
@Getter
public class BaseConnectionDTO extends BaseEntity implements Connection {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "englishName must be not NULL")
    @NotBlank(message = "englishName must be not BLANK")
    private String englishName;

    @NotNull(message = "vietnameseName must be not NULL")
    @NotBlank(message = "vietnameseName must be not BLANK")
    private String vietnameseName;

    @EnumNamePattern(regexp = "FTP|FTPS|HTTP", message = "protocol must be in {FTP, FTPS, HTTP}")
    private Protocol protocol;

    public BaseConnectionDTO(Long id) {
        super(id);
    }

    public BaseConnectionDTO(Long id, String englishName, String vietnameseName, Protocol protocol) {
        super(id);
        this.englishName = englishName;
        this.vietnameseName = vietnameseName;
        this.protocol = protocol;
    }
}
