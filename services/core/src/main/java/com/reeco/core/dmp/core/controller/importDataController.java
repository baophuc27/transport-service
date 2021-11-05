package com.reeco.core.dmp.core.controller;

import com.reeco.core.dmp.core.dto.ImportData;
import com.reeco.core.dmp.core.service.ImportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/data")
public class importDataController {
    @Autowired
    ImportDataService importDataService;

    @PostMapping("/import-csv")
    public String importDataCsv(@ModelAttribute ImportData importData) throws Exception{
//        importDataService.recieveDataCsv(importData.getCsvFile(), importData.getOrganizationId());
//        file.flatMap(filee-> importDataService.recieveDataCsv(filee, orgId));
        return "aaaaaaa";
    }
}
