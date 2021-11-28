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

    private Long stationId;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private String updatedAt;

    public Parameter() {
    }

    public Parameter(Long id,
                     Long connectionId,
                     Long organizationId,
                     Long stationId,
                     String englishName,
                     String vietnameseName,
                     ParamType parameterType,
                     Long indicatorId,
                     String keyName,
                     String displayType,
                     String unit, String format,
                     @Valid List<Alarm> alarms,
                     String receivedAt) {
        this.id = id;
        this.connectionId = connectionId;
        this.organizationId = organizationId;
        this.stationId = stationId;
        this.englishName = englishName;
        this.vietnameseName = vietnameseName;
        this.parameterType = parameterType;
        this.indicatorId = indicatorId;
        this.keyName = keyName;
        this.displayType = displayType;
        this.unit = unit;
        this.format = format;
        this.alarms = alarms;
        this.receivedAt = receivedAt;
    }

    @Override
    public String toString() {
        return "Parameter{" +
                "id=" + id +
                ", connectionId=" + connectionId +
                ", organizationId=" + organizationId +
                ", stationId=" + stationId +
                ", englishName='" + englishName + '\'' +
                ", vietnameseName='" + vietnameseName + '\'' +
                ", parameterType=" + parameterType +
                ", indicatorId=" + indicatorId +
                ", keyName='" + keyName + '\'' +
                ", displayType='" + displayType + '\'' +
                ", unit='" + unit + '\'' +
                ", format='" + format + '\'' +
                ", alarms=" + alarms +
                ", receivedAt='" + receivedAt + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Parameter)) return false;
        Parameter parameter = (Parameter) o;
        return Objects.equals(getId(), parameter.getId()) &&
                Objects.equals(getConnectionId(), parameter.getConnectionId()) &&
                Objects.equals(getOrganizationId(), parameter.getOrganizationId()) &&
                Objects.equals(getStationId(), parameter.getStationId()) &&
                Objects.equals(getEnglishName(), parameter.getEnglishName()) &&
                Objects.equals(getVietnameseName(), parameter.getVietnameseName()) &&
                getParameterType() == parameter.getParameterType() &&
                Objects.equals(getIndicatorId(), parameter.getIndicatorId()) &&
                Objects.equals(getKeyName(), parameter.getKeyName()) &&
                Objects.equals(getDisplayType(), parameter.getDisplayType()) &&
                Objects.equals(getUnit(), parameter.getUnit()) &&
                Objects.equals(getFormat(), parameter.getFormat()) &&
                Objects.equals(getAlarms(), parameter.getAlarms()) &&
                Objects.equals(getReceivedAt(), parameter.getReceivedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getConnectionId(), getOrganizationId(), getStationId(), getEnglishName(), getVietnameseName(), getParameterType(), getIndicatorId(), getKeyName(), getDisplayType(), getUnit(), getFormat(), getAlarms(), getReceivedAt());
    }
}
