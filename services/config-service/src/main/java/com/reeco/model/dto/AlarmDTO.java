package com.reeco.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reeco.framework.EnumNamePattern;
import com.reeco.model.AlarmType;
import com.reeco.model.BaseEntity;
import com.reeco.model.Entity;
import com.reeco.model.Protocol;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlarmDTO extends BaseEntity implements Entity {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "englishName must be not NULL")
    @NotBlank(message = "englishName must be not BLANK")
    private String englishName;

    @NotNull(message = "vietnameseName must be not NULL")
    @NotBlank(message = "vietnameseName must be not BLANK")
    private String vietnameseName;

    @EnumNamePattern(regexp = "THRESHOLD|RANGE", message = "alarmType must be in {THRESHOLD, RANGE}")
    private AlarmType alarmType;

    private Double eqValue;

    private Double minValue;

    private Double maxValue;

    private String compareType;

    public AlarmDTO() {
        super(-1L);
    }

    public AlarmDTO(Long id) {
        super(id);
    }

    public AlarmDTO(Long id,
                    String englishName,
                    String vietnameseName,
                    AlarmType alarmType,
                    Double eqValue,
                    Double minValue,
                    Double maxValue,
                    String compareType) {
        super(id);
        this.englishName = englishName;
        this.vietnameseName = vietnameseName;
        this.alarmType = alarmType;
        this.eqValue = eqValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.compareType = compareType;
    }

    @Override
    public String toString() {
        return "AlarmDTO{" +
                "englishName='" + englishName + '\'' +
                ", vietnameseName='" + vietnameseName + '\'' +
                ", alarmType=" + alarmType +
                ", eqValue=" + eqValue +
                ", minValue=" + minValue +
                ", maxValue=" + maxValue +
                ", compareType='" + compareType + '\'' +
                "} " + super.toString();
    }
}
