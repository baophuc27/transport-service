package com.reeco.ingestion.application.port.in;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.reeco.common.model.enumtype.ParamType;
import com.reeco.ingestion.domain.Parameter;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class ParameterDTO {

    @JsonProperty("id")
    private String paramId;
    private String englishName;
    private String vietnameseName;
    private Parameter.ParamType parameterType;
    private Long indicatorId;
    private String keyName;

    private String displayType;

    @NotNull(message = "unit must be not NULL")
    @NotBlank(message = "unit must be not BLANK")
    private String unit;

    private String format;

    @Valid
    private List<AlarmDTO> alarms;

    public ParameterDTO() {
        super(-1L);
    }

    public ParameterDTO(Long id) {
        super(id);
    }

    public ParameterDTO(Long id, String englishName,
                        String vietnameseName,
                        ParamType parameterType,
                        Long indicatorId,
                        String keyName,
                        String displayType,
                        String unit,
                        String format,
                        List<AlarmDTO> alarms) {
        super(id);
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
                "englishName='" + englishName + '\'' +
                ", vietnameseName='" + vietnameseName + '\'' +
                ", parameterType=" + parameterType +
                ", indicatorId=" + indicatorId +
                ", keyName='" + keyName + '\'' +
                ", displayType='" + displayType + '\'' +
                ", unit='" + unit + '\'' +
                ", format='" + format + '\'' +
                ", alarms=" + alarms +
                "} " + super.toString();
    }
}
