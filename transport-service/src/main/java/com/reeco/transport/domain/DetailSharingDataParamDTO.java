package com.reeco.transport.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DetailSharingDataParamDTO {
    Long indicatorId;

    Long paramId;

    String sourceName;

    String englishName;

    String vietnameseName;

    String unit;
}
