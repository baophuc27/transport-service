package com.reeco.core.dmp.core.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.reeco.core.dmp.core.model.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/chart")
public class chartController {
    @PostMapping("/history-data")
    public Mono<String> getChartHistory() {
        
        return  Mono.just("chartDto1");
    }    
}
