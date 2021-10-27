package com.reeco.core.dmp.core.controller;

import com.reeco.core.dmp.core.dto.ChartDto;
import com.reeco.core.dmp.core.dto.DataPointDto;
import com.reeco.core.dmp.core.dto.ParameterDataDto;
import com.reeco.core.dmp.core.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.reeco.core.dmp.core.model.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/chart")
public class chartController {
    @Autowired
    ChartService chartService;

    @PostMapping("/history-data")
    public Mono<ChartDto> getChartHistory(@RequestBody ChartDto chartDto) throws  Exception{
        return chartService.historyData(chartDto);
    }

    @PostMapping("/allData")
    public Mono<ChartDto> allData(@RequestBody ChartDto chartDto) throws Exception{
        return  chartService.allData(chartDto);
    }
}
