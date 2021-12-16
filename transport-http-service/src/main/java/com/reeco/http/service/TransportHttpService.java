package com.reeco.http.service;

import com.reeco.common.model.dto.IncomingTsEvent;
import com.reeco.http.cache.ConnectionCache;
import com.reeco.http.model.dto.Connection;
import com.reeco.http.model.dto.Parameter;
import com.reeco.http.model.dto.RequestDto;
import com.reeco.http.until.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class TransportHttpService {
    @Autowired
    ConnectionCache connectionCache;

    @Autowired
    private KafkaTemplate<String, IncomingTsEvent> kafkaProducerEventTemplate;

    public ApiResponse pushDataToKafka(RequestDto requestDto, String accessKey) throws Exception{
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();
//        Todo: Read param info from cache and push to Kafka
        String connectionId = accessKey.split("%")[0];
        Connection connection = connectionCache.get(connectionId);

        IncomingTsEvent msg = new IncomingTsEvent();
        LocalDateTime currTime = LocalDateTime.now();
        Long requestDtoParamId = requestDto.getParamId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        List<Parameter> paramList = connection.getParameterList();
        for (Parameter param : paramList) {
            if (param.getParamId() == requestDtoParamId) {
                msg.setOrganizationId(param.getOrgId());
                msg.setWorkspaceId(param.getWorkspaceId());
                msg.setStationId(param.getStationId());
                msg.setConnectionId(Long.valueOf(connectionId));
                msg.setParamId(requestDtoParamId);
                msg.setEventTime(LocalDateTime.parse(requestDto.getEventTime(), formatter));
                msg.setIndicatorId(param.getIndicatorId());
                msg.setIndicatorName(param.getIndicatorName());
                msg.setParamName(param.getParamName());
                msg.setValue(requestDto.getValue());
                msg.setReceivedAt(currTime);
                msg.setSentAt(currTime);
                msg.setLat(requestDto.getLat());
                msg.setLon(requestDto.getLon());
            }
        }
        log.info(String.valueOf(msg));
        kafkaProducerEventTemplate.send("reeco_time_series_event", msg);

        apiResponse.setMessage("Successful!");
        return apiResponse;
    }
}
