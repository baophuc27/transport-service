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
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    private KafkaTemplate<String, ParameterCache> configProducerEventTemplate;

    private final String CONFIG_TOPIC_NAME = "reeco_config_event";

    public ApiResponse pushDataToKafka(RequestDto requestDto, String accessKey, String connectionId) throws Exception{
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();
//        Todo: Read param info from cache and push to Kafka
        Connection connection = connectionCache.get(connectionId+"%"+accessKey);
        for (RequestParam paramRequest: requestDto.getParams()) {
            IncomingTsEvent msg = new IncomingTsEvent();
            LocalDateTime currTime = LocalDateTime.now();
            String requestDtoParamKey = paramRequest.getKey();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            ParameterCache paraMsg = new ParameterCache();


            List<ParameterCache> paramList = connection.getParameterList();
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
                throw new Exception("Invalid access token!");
            }
            log.info(String.valueOf(msg));
            kafkaProducerEventTemplate.send("reeco_time_series_event", msg);
        }
        apiResponse.setMessage("Successful!");
        return apiResponse;
    }

    public ApiResponse getAccessToken(Long connectionId) throws Exception{
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();

        Connection connection = connectionCache.get(connectionId.toString());
        if (connection==null){
            throw new Exception("Invalid connection Id.");
        }
        apiResponse.setData(connection.getAccessToken());
        return apiResponse;
    }


}
