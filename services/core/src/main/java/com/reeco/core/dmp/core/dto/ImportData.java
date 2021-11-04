package com.reeco.core.dmp.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import org.springframework.web.multipart.MultipartFile;

@Data
public class ImportData {
    Long organizationId;

    MultipartFile csvFile;
}
