package com.reeco.core.dmp.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.core.dmp.core.dto.*;
import com.reeco.core.dmp.core.service.ChartService;
import com.reeco.core.dmp.core.service.DataService;
import com.reeco.core.dmp.core.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
//import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/data")
public class DataController {
    @Autowired
    DataService dataService;

    @Autowired
    ChartService chartService;

    @PostMapping("/import-csv")
    public ResponseEntity<ResponseMessage> importDataCsv(@ModelAttribute ImportData importData) throws Exception{

        String str = importData.getParamsInfo();
        if (! str.startsWith("[") || ! str.endsWith("]"))
            throw new IllegalArgumentException("Bad data: " + str);
        String[] strArray = str.substring(1, str.length() - 1).split("},", -1);
        List<ParameterDto> parameterDtoList = new ArrayList<>();
        Integer i=1;
        for (String param: strArray){
            ObjectMapper mapper = new ObjectMapper();
            if(i<strArray.length){
                param = param +"}";
            }
            ParameterDto parameterDto = mapper.readValue(param, ParameterDto.class);
            parameterDtoList.add(parameterDto);
            i=i+1;
        }
        dataService.receiveDataCsv(importData.getCsvFile(), importData.getOrganizationId(), importData.getStationId(),parameterDtoList);
//        file.flatMap(filee-> importDataService.receiveDataCsv(filee, orgId));
        return ResponseEntity.ok().body(new ResponseMessage("Import data successful!"));
    }

    @GetMapping("/export-csv")
    public ResponseEntity exportDataCsv(@RequestBody ChartDto chartDto) throws Exception{
        String template = dataService.exportDataCsv(chartDto);
        String csvFileName = "data-export.csv";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+csvFileName);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(template));
    }

    @GetMapping("/connection/latest-data")
    public ResponseEntity<ApiResponse> getLatestDataConnection(@RequestParam Long orgId,@RequestParam List<Long> connectionIds) throws Exception{
        ApiResponse apiResponse = dataService.getLatestDataConnection(orgId,connectionIds);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @GetMapping("/connection/history")
    public ResponseEntity<ApiResponse> getConnectionHistory(@RequestParam Long orgId, @RequestParam Long connectionId,@RequestParam(required = false) String status, @RequestParam(required = false) Integer startIndex, @RequestParam(required = false) Integer endIndex, @RequestParam(required = false) Long startTime, @RequestParam(required = false) Long endTime, @RequestParam(required = false) Long alarmId) throws Exception{
        ApiResponse apiResponse = dataService.getConnectionHistory(orgId,connectionId,status,startIndex,endIndex,startTime,endTime,alarmId);
        return new ResponseEntity<>(apiResponse,apiResponse.getStatus());
    }

    @DeleteMapping("/connection/history")
    public ResponseEntity<ApiResponse> deleteConnectionHistory(@RequestParam Long orgId, @RequestParam Long connectionId, @RequestParam(required = false) Long index){
        ApiResponse apiResponse = dataService.deleteConnectionHistory(orgId,connectionId,index);
        return new ResponseEntity<>(apiResponse,apiResponse.getStatus());
    }

    @PostMapping("/dataAlarm")
    public ResponseEntity<ApiResponse> getAlarmData(@RequestBody ChartDto chartDto) throws Exception{
        ApiResponse apiResponse = chartService.getAlarmData(chartDto);
        return new ResponseEntity<>(apiResponse,apiResponse.getStatus());
    }
}
