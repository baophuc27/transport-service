package com.reeco.shares.api.dmp.view;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class IndicatorDto {

    private Long id;

    private String name;

    private String type;

    private String key;
}
