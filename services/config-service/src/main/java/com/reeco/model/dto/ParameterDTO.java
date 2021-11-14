package com.reeco.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reeco.framework.EnumNamePattern;
import com.reeco.model.*;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class ParameterDTO extends BaseEntity implements Parameter {

    @NotNull(message = "englishName must be not NULL")
    @NotBlank(message = "englishName must be not BLANK")
    private String englishName;

    @NotNull(message = "vietnameseName must be not NULL")
    @NotBlank(message = "vietnameseName must be not BLANK")
    private String vietnameseName;

    @EnumNamePattern(regexp = "CONFIGURE|COMPUTED", message = "parameterType must be in {CONFIGURE, COMPUTED}")
    private ParamType parameterType;

    @NotNull(message = "indicatorId must be not NULL")
    private Long indicatorId;

    @NotNull(message = "keyName must be not NULL")
    @NotBlank(message = "keyName must be not BLANK")
    private String keyName;

    private String displayType;

    @NotNull(message = "unit must be not NULL")
    @NotBlank(message = "unit must be not BLANK")
    private String unit;

    private String format;

    public ParameterDTO() {
        super(-1L);
    }

    public ParameterDTO(Long id) {
        super(id);
    }

    public ParameterDTO(Long id, String englishName, String vietnameseName, ParamType parameterType, Long indicatorId, String keyName, String displayType, String unit, String format) {
        super(id);
        this.englishName = englishName;
        this.vietnameseName = vietnameseName;
        this.parameterType = parameterType;
        this.indicatorId = indicatorId;
        this.keyName = keyName;
        this.displayType = displayType;
        this.unit = unit;
        this.format = format;
    }

    @Override
    public String toString() {
        return "ParameterDTO{" +
                "englishName='" + englishName + '\'' +
                ", vietnameseName='" + vietnameseName + '\'' +
                ", parameterType=" + parameterType +
                ", indicatorId=" + indicatorId +
                ", keyName='" + keyName + '\'' +
                ", displayType='" + displayType + '\'' +
                ", unit='" + unit + '\'' +
                ", format='" + format + '\'' +
                "} " + super.toString();
    }
}
