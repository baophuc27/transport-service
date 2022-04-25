package com.reeco.http.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RequestDto {

    private String status;

    private List<Param> params;

    @Data
    @NoArgsConstructor
    public class Param{
        private String key;

        private String value;

        private String unit;

    }
}
