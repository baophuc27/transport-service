package com.reeco.http.controller;

import com.reeco.http.cache.ConnectionCache;
import com.reeco.http.model.dto.Parameter;
import com.reeco.http.model.dto.RequestDto;
import com.reeco.http.service.TransportHttpService;
import com.reeco.http.until.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(value = {"*"})
@RestController()
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/reeco")
public class TransportHttpController {

    @Autowired
    ConnectionCache connectionCache;

    @Autowired
    TransportHttpService transportHttpService;

    @RequestMapping("/receive-data")
    public ResponseEntity<ApiResponse> receiveData(@RequestHeader("access_key") String accessKey, @RequestBody RequestDto requestDto) throws Exception{
        ApiResponse apiResponse = transportHttpService.pushDataToKafka(requestDto, accessKey);
        return new ResponseEntity<>(apiResponse,apiResponse.getStatus());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        connectionCache.loadDataToCache();
        log.info("Load Connection to cache manager when start up");
    }

}
