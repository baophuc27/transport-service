package com.reeco.transport.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class SharingDataDTO {
    Long userId;

    @NotNull
    Long requestId;

    @NotNull
    Long stationId;

    @NotNull
    MultipartFile csvFile;
}