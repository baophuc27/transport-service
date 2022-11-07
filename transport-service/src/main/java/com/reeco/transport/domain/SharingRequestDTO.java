package com.reeco.transport.domain;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
public class    SharingRequestDTO {
    @NotNull
    Long userId;

    @NotNull
    Long requestId;

    @NotNull
    Long stationId;

    @NotNull
    String userName;

    @NotNull
    String requestTitle;

    @NotNull
    List<DataSharingParamDto> indicatorList;

    Double lat;
    Double lon;
}
