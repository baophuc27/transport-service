package com.reeco.core.dmp.core.dto;

import lombok.Data;

import org.springframework.web.multipart.MultipartFile;

@Data
public class ImportData {
    Long organizationId;

    Long stationId;

    String paramsInfo;

    MultipartFile csvFile;
}
