package com.reeco.common.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reeco.common.model.enumtype.Permission;
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
public class APIKey {

    @NotNull(message = "id must not be NULL")
    @NotBlank(message = "id must not be BLANK")
    private String id;

    private String apiKey;

    private Permission permission;

    private Scope scope;

    public APIKey() {
    }
}
