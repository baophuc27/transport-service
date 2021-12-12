package com.reeco.http.service;

import com.reeco.http.cache.ConnectionCache;
import com.reeco.http.model.dto.Connection;
import com.reeco.http.model.dto.RequestDto;
import com.reeco.http.until.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class TransportHttpService {
    @Autowired
    ConnectionCache connectionCache;

    public ApiResponse pushDataToKafka(RequestDto requestDto, String accessKey) throws Exception{
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();
//        Todo: Read param info from cache and push to Kafka
        String connectionId = accessKey.split("%")[0];
        Connection connection = connectionCache.get(connectionId);


        apiResponse.setMessage("Successful!");
        return apiResponse;
    }
}
