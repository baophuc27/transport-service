package com.reeco.common.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reeco.common.framework.EnumNamePattern;
import com.reeco.common.model.enumtype.AlarmType;
import com.reeco.common.model.enumtype.CustomIdType;
import com.reeco.common.model.enumtype.Protocol;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reeco.common.model.enumtype.Protocol;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;


@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomId {

    private static final long serialVersionUID = 1L;

    private String customId;


    @EnumNamePattern(regexp = "WORKSPACE|CONNECTION|STATION|PARAMETER",
            message = "idType must be in {WORKSPACE,CONNECTION,STATION,PARAMETER}")
    private CustomIdType customIdType;

    @NotNull(message="originalId must not be NULL")
    @NotBlank(message = "originalId must not be BLANK")
    private String originalId;
}
