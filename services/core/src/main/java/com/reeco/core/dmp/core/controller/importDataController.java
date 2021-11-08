package com.reeco.core.dmp.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.core.dmp.core.dto.ImportData;
import com.reeco.core.dmp.core.dto.ParameterDto;
import com.reeco.core.dmp.core.dto.ParamsInfo;
import com.reeco.core.dmp.core.dto.ResponseMessage;
import com.reeco.core.dmp.core.service.ImportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/data")
public class importDataController {
    @Autowired
    ImportDataService importDataService;

    @PostMapping("/import-csv")
    public ResponseEntity<ResponseMessage> importDataCsv(@ModelAttribute ImportData importData) throws Exception{

        String str = importData.getParamsInfo();
        if (! str.startsWith("[") || ! str.endsWith("]"))
            throw new IllegalArgumentException("Bad data: " + str);
        String[] strArray = str.substring(1, str.length() - 1).split("},", -1);
        List<ParameterDto> parameterDtoList = new ArrayList<>();
        Integer i=0;
        for (String param: strArray){
            ObjectMapper mapper = new ObjectMapper();
            if(i<strArray.length){
                param = param +"}";
            }
            ParameterDto parameterDto = mapper.readValue(param, ParameterDto.class);
            parameterDtoList.add(parameterDto);
            i=i+1;
        }
        importDataService.recieveDataCsv(importData.getCsvFile(), importData.getOrganizationId(), importData.getStationId(),parameterDtoList);
//        file.flatMap(filee-> importDataService.recieveDataCsv(filee, orgId));
        return ResponseEntity.ok().body(new ResponseMessage("Import data successful!"));
    }
}
