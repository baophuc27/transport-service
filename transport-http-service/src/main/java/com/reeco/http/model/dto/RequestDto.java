package com.reeco.http.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestDto {

    private Long paramId;

    private String value;

    private String eventTime;

    private Double lat;

    private Double lon;
}
