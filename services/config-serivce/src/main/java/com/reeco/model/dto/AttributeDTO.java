package com.reeco.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reeco.model.*;
import lombok.Getter;

import javax.validation.constraints.NotNull;


@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class AttributeDTO extends BaseEntity implements Attribute {

    private String englishName;

    private String vietnameseName;

    @NotNull
    private AttributeType parameterType;

    private String indicator;

    private String keyName;

    private String displayType;

    private String unit;

    private String format;

    public AttributeDTO() {
        super(-1L);
    }

    public AttributeDTO(Long id) {
        super(id);
    }

    public AttributeDTO(Long id, String englishName, String vietnameseName, AttributeType parameterType, String indicator, String keyName, String displayType, String unit, String format) {
        super(id);
        this.englishName = englishName;
        this.vietnameseName = vietnameseName;
        this.parameterType = parameterType;
        this.indicator = indicator;
        this.keyName = keyName;
        this.displayType = displayType;
        this.unit = unit;
        this.format = format;
    }
}
