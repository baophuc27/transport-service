package com.reeco.common.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scope {
    private List<Integer> organizationIds;

    private List<Integer> parameterIds;

    private List<Integer> connectionIds;

    public Scope() {
    }
}
