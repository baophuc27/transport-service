package com.reeco.http.service;

import com.reeco.common.model.dto.IncomingTsEvent;
import com.reeco.http.cache.ConnectionCache;
import com.reeco.http.model.dto.Connection;
import com.reeco.http.model.dto.RequestDto;
import com.reeco.http.model.dto.ParameterCache;
import com.reeco.http.model.dto.RequestParam;
import com.reeco.http.model.repo.ParamsByOrgRepository;
import com.reeco.http.until.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TransportHttpService {
    @Autowired
    ConnectionCache connectionCache;

    @Autowired
    ParamsByOrgRepository paramsByOrgRepository;
    @Autowired
    private KafkaTemplate<String, IncomingTsEvent> kafkaProducerEventTemplate;

    public ApiResponse pushDataToKafka(RequestDto requestDto, String accessKey, String connectionId) throws Exception{
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();
//        Todo: Read param info from cache and push to Kafka

        List<IncomingTsEvent> allTsEvent = new ArrayList<>();
        Connection connection = connectionCache.get(connectionId+"%"+accessKey);
        log.info("Get from cache key: {} connection: {}",connectionId+"%"+accessKey,connection.toString());
        for (RequestParam paramRequest: requestDto.getParams()) {
            IncomingTsEvent msg = new IncomingTsEvent();
            LocalDateTime currTime = LocalDateTime.now();
            String requestDtoParamKey = paramRequest.getKey();

            List<ParameterCache> paramList = connection.getParameterList();

            // TODO: should use mapper if I had enough time :)), below is a bad practice, please don't follow.
            for (ParameterCache param : paramList) {
                if (param.getParamKey().equals(requestDtoParamKey)) {
                    msg.setOrganizationId(param.getOrgId());
                    msg.setWorkspaceId(param.getWorkspaceId());
                    msg.setStationId(param.getStationId());
                    msg.setConnectionId(Long.valueOf(connectionId));
                    msg.setParamId(param.getParamId());
                    msg.setEventTime(currTime);
                    msg.setIndicatorId(param.getIndicatorId());
                    msg.setIndicatorName(param.getIndicatorName());
                    msg.setParamName(param.getParamName());
                    msg.setValue(paramRequest.getValue());
                    msg.setReceivedAt(currTime);
                    msg.setSentAt(currTime);
                }
            }

            if (msg.getParamId() == null) {
                apiResponse.setStatus(HttpStatus.BAD_REQUEST);
                apiResponse.setMessage("Not match parameter: "+requestDtoParamKey);
                return apiResponse;
            }
            allTsEvent.add(msg);
        }

        for (IncomingTsEvent event : allTsEvent){
            kafkaProducerEventTemplate.send("reeco_time_series_event", event);
            log.info("Pushed message: {}",event);
        }

        apiResponse.setStatus(HttpStatus.OK);
        apiResponse.setMessage("Successful!");
        return apiResponse;
    }

    public ApiResponse getAccessToken(Long connectionId) throws Exception{
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();

        Connection connection = connectionCache.get(connectionId.toString());
        if (connection==null){
            apiResponse.setStatus(HttpStatus.BAD_REQUEST);
            apiResponse.setData("Invalid connection id.");
            return apiResponse;
        }
        apiResponse.setStatus(HttpStatus.OK);
        apiResponse.setMessage(connection.getAccessToken());
        return apiResponse;
    }


}
