package com.reeco.common.model.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reeco.common.framework.EnumNamePattern;
import com.reeco.common.model.enumtype.ParamType;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Parameter implements Serializable {

    @NotNull(message = "id must be not NULL")
    private Long id;

    @NotNull(message = "connectionId must be not NULL")
    private Long connectionId;

    @NotNull(message = "organizationId must be not NULL")
    private Long organizationId;

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

    @Valid
    private List<Alarm> alarms;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private String receivedAt;

    public Parameter() {
    }


    public Parameter(Long id,
                     Long connectionId,
                     Long organizationId,
                     String englishName,
                     String vietnameseName,
                     ParamType parameterType,
                     Long indicatorId,
                     String keyName, String displayType,
                     String unit,
                     String format,
                     List<Alarm> alarms) {
        this.id = id;
        this.connectionId = connectionId;
        this.organizationId = organizationId;
        this.englishName = englishName;
        this.vietnameseName = vietnameseName;
        this.parameterType = parameterType;
        this.indicatorId = indicatorId;
        this.keyName = keyName;
        this.displayType = displayType;
        this.unit = unit;
        this.format = format;
        this.alarms = alarms;
    }

    @Override
    public String toString() {
        return "ParameterDTO{" +
                "id=" + id +
                ", connectionId=" + connectionId +
                ", organizationId=" + organizationId +
                ", englishName='" + englishName + '\'' +
                ", vietnameseName='" + vietnameseName + '\'' +
                ", parameterType=" + parameterType +
                ", indicatorId=" + indicatorId +
                ", keyName='" + keyName + '\'' +
                ", displayType='" + displayType + '\'' +
                ", unit='" + unit + '\'' +
                ", format='" + format + '\'' +
                ", alarms=" + alarms +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parameter)) return false;
        Parameter that = (Parameter) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getConnectionId(), that.getConnectionId()) &&
                Objects.equals(getOrganizationId(), that.getOrganizationId()) &&
                Objects.equals(getEnglishName(), that.getEnglishName()) &&
                Objects.equals(getVietnameseName(), that.getVietnameseName()) &&
                getParameterType() == that.getParameterType() &&
                Objects.equals(getIndicatorId(), that.getIndicatorId()) &&
                Objects.equals(getKeyName(), that.getKeyName()) &&
                Objects.equals(getDisplayType(), that.getDisplayType()) &&
                Objects.equals(getUnit(), that.getUnit()) &&
                Objects.equals(getFormat(), that.getFormat()) &&
                Objects.equals(getAlarms(), that.getAlarms());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getConnectionId(), getOrganizationId(), getEnglishName(), getVietnameseName(), getParameterType(), getIndicatorId(), getKeyName(), getDisplayType(), getUnit(), getFormat(), getAlarms());
    }


}
