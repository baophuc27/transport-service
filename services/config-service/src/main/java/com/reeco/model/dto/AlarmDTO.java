package com.reeco.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reeco.framework.EnumNamePattern;
import com.reeco.model.*;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlarmDTO extends BaseEntity implements Entity {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "englishName of Alarm must be not NULL")
    @NotBlank(message = "englishName of Alarm be not BLANK")
    private String englishName;

    @NotNull(message = "vietnameseName of Alarm must be not NULL")
    @NotBlank(message = "vietnameseName of Alarm must be not BLANK")
    private String vietnameseName;

    @EnumNamePattern(regexp = "THRESHOLD|SQUARE_RANGE|BRACKET_RANGE", message = "alarmType must be in {THRESHOLD,SQUARE_RANGE,BRACKET_RANGE}")
    private AlarmType alarmType;

    private Double minValue;

    private Double maxValue;

    private MaintainType maintainType;

    private Integer numOfMatch;

    private Long frequence;

    private FrequenceType frequenceType;

    public AlarmDTO() {
        super(-1L);
    }

    public AlarmDTO(Long id) {
        super(id);
    }

    public AlarmDTO(Long id,
                    String englishName,
                    String vietnameseName,
                    @EnumNamePattern(regexp = "THRESHOLD|RANGE", message = "alarmType must be in {THRESHOLD, RANGE}") AlarmType alarmType,
                    Double minValue,
                    Double maxValue,
                    @EnumNamePattern(regexp = "NONE|FISRTTIME|MAINTAIN", message = "maintainType must be in {NONE, FISRTTIME, MAINTAIN}") MaintainType maintainType,
                    Integer numOfMatch,
                    Long frequence,
                    @EnumNamePattern(regexp = "S|M|H|D", message = "maintainType must be in {S, M, H, D}") FrequenceType frequenceType) {
        super(id);
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
        return "AlarmDTO{" +
                "englishName='" + englishName + '\'' +
                ", vietnameseName='" + vietnameseName + '\'' +
                ", alarmType=" + alarmType +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", maintainType=" + maintainType +
                ", numOfMatch=" + numOfMatch +
                ", frequence=" + frequence +
                ", frequenceType=" + frequenceType +
                "} " + super.toString();
    }
}
