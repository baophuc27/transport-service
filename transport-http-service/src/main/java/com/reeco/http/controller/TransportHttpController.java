package com.reeco.http.controller;

import com.reeco.http.cache.ConnectionCache;
import com.reeco.http.model.dto.RequestDto;
import com.reeco.http.until.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(value = {"*"})
@RestController()
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/reeco")
public class TransportHttpController {

    @Autowired
    ConnectionCache connectionCache;

    @RequestMapping("/recive-data")
    public ResponseEntity<ApiResponse> reciveData(@RequestBody RequestDto requestDto) throws Exception{
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();
        apiResponse.setMessage("ok");
        return new ResponseEntity<>(apiResponse,apiResponse.getStatus());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        connectionCache.loadDataToCache();
        log.info("Load Connection to cache manager when start up");
    }

}
