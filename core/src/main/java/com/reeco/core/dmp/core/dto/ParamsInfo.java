package com.reeco.core.dmp.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ParamsInfo {
    private List<ParameterDto> paramsInfo;
}
