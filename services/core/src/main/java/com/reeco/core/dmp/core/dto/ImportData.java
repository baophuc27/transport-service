package com.reeco.core.dmp.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ImportData {
    Long organizationId;

    Long stationId;

    String paramsInfo;

    MultipartFile csvFile;
}
