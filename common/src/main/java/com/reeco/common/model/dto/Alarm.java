package com.reeco.common.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.reeco.common.framework.EnumNamePattern;
import com.reeco.common.model.enumtype.AlarmType;
import com.reeco.common.model.enumtype.FrequenceType;
import com.reeco.common.model.enumtype.MaintainType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class Alarm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "alarmId must be note NULL")
    private Long id;

    @NotNull(message = "organizationId must be not NULL")
    private Long organizationId;

    @NotNull(message = "paramId must be not NULL")
    private Long paramId;

    @NotNull(message = "englishName of Alarm must be not NULL")
    @NotBlank(message = "englishName of Alarm be not BLANK")
    private String englishName;

    @NotNull(message = "vietnameseName of Alarm must be not NULL")
    @NotBlank(message = "vietnameseName of Alarm must be not BLANK")
    private String vietnameseName;

    @EnumNamePattern(regexp = "THRESHOLD|SQUARE_RANGE|BRACKET_RANGE",
            message = "alarmType must be in {THRESHOLD,SQUARE_RANGE,BRACKET_RANGE}")
    private AlarmType alarmType;

    private String minValue;

    private String maxValue;

    private MaintainType maintainType;

    private Long numOfMatch;

    private Long frequence;

    private FrequenceType frequenceType;

    @Getter
    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private String receivedAt;

    public Alarm() {
    }

    public Alarm(@NotNull(message = "alarmId must be note NULL") Long id) {
        this.id = id;
    }

    public Alarm(Long organizationId, Long paramId,
                 Long id,
                 String englishName,
                 String vietnameseName,
                 AlarmType alarmType,
                 String minValue,
                 String maxValue,
                 MaintainType maintainType,
                 Long numOfMatch,
                 Long frequence,
                 FrequenceType frequenceType) {

        this.id = id;
        this.organizationId = organizationId;
        this.paramId = paramId;
        this.englishName = englishName;
        this.vietnameseName = vietnameseName;
        this.alarmType = alarmType;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.maintainType = maintainType;
        this.numOfMatch = numOfMatch;
        this.frequence = frequence;
        this.frequenceType = frequenceType;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "alarmId=" + id +
                ", organizationId=" + organizationId +
                ", paramId=" + paramId +
                ", englishName='" + englishName + '\'' +
                ", vietnameseName='" + vietnameseName + '\'' +
                ", alarmType=" + alarmType +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", maintainType=" + maintainType +
                ", numOfMatch=" + numOfMatch +
                ", frequence=" + frequence +
                ", frequenceType=" + frequenceType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alarm)) return false;
        Alarm alarm = (Alarm) o;
        return Objects.equals(getId(), alarm.getId()) &&
                Objects.equals(getOrganizationId(), alarm.getOrganizationId()) &&
                Objects.equals(getParamId(), alarm.getParamId()) &&
                Objects.equals(getEnglishName(), alarm.getEnglishName()) &&
                Objects.equals(getVietnameseName(), alarm.getVietnameseName()) &&
                getAlarmType() == alarm.getAlarmType() &&
                Objects.equals(getMinValue(), alarm.getMinValue()) &&
                Objects.equals(getMaxValue(), alarm.getMaxValue()) &&
                getMaintainType() == alarm.getMaintainType() &&
                Objects.equals(getNumOfMatch(), alarm.getNumOfMatch()) &&
                Objects.equals(getFrequence(), alarm.getFrequence()) &&
                getFrequenceType() == alarm.getFrequenceType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOrganizationId(), getParamId(), getEnglishName(), getVietnameseName(), getAlarmType(), getMinValue(), getMaxValue(), getMaintainType(), getNumOfMatch(), getFrequence(), getFrequenceType());
    }
}
