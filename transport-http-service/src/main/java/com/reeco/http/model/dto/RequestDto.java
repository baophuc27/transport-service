package com.reeco.http.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class RequestDto {

    private String status;

    private List<RequestParam> params;
}
