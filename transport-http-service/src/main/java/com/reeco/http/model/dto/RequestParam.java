package com.reeco.http.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestParam {
    private String key;

    private String value;

    private String unit;
}
