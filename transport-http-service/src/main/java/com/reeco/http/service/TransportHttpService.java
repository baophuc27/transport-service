package com.reeco.http.service;

import com.reeco.common.model.dto.HTTPConnection;
import com.reeco.common.model.dto.IncomingTsEvent;
import com.reeco.http.cache.ConnectionCache;
import com.reeco.http.infrastructure.persistence.postgresql.entity.HTTPConnectionMetadata;
import com.reeco.http.infrastructure.persistence.postgresql.repository.HTTPConnectionMetadataRepository;
import com.reeco.http.mapper.HTTPConnectionMapper;
import com.reeco.http.model.*;
import com.reeco.http.infrastructure.persistence.cassandra.repository.ParamsByOrgRepository;
import com.reeco.http.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TransportHttpService {
    @Autowired
    ConnectionCache connectionCache;

    @Autowired
    ParamsByOrgRepository paramsByOrgRepository;

    @Autowired
    HTTPConnectionMapper httpConnectionMapper;

    @Autowired
    HTTPConnectionMetadataRepository httpConnectionMetadataRepository;

    @Autowired
    private KafkaTemplate<String, IncomingTsEvent> kafkaProducerEventTemplate;

    @Autowired
    private KafkaTemplate<String, HTTPAlarmMessage> alarmMessageKafkaTemplate;

    @Value(value = "${kafka.topics.conn-alarm-topic.name}")
    private String connectionAlarmTopic;

    @Value(value = "${kafka.topics.event-topic.name}")
    private String eventTopic;

    public ApiResponse pushDataToKafka(RequestDto requestDto, String accessKey, String connectionId) throws Exception{
        ApiResponse apiResponse = ApiResponse.getSuccessResponse();

        List<IncomingTsEvent> allTsEvent = new ArrayList<>();
        Connection connection = connectionCache.get(connectionId + "%" + accessKey);
        Optional<HTTPConnectionMetadata> httpConnectionMetadataOptional =
                httpConnectionMetadataRepository.findById(Long.parseLong(connectionId));

        log.info("Get from cache key: {} connection: {}",connectionId+"%"+accessKey,connection.toString());

        for (RequestParam paramRequest: requestDto.getParams()) {
            IncomingTsEvent msg = new IncomingTsEvent();
            LocalDateTime currTime = LocalDateTime.now();
            LocalDateTime eventTime = paramRequest.getTimestamp();
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
                    msg.setEventTime(eventTime);
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
            kafkaProducerEventTemplate.send(eventTopic, event);
            log.info("Pushed message: {}",event);
        }

        // Update lastActive time on HTTP Connection metadata
        if (httpConnectionMetadataOptional.isPresent()) {
            HTTPConnectionMetadata httpConnectionMetadata = httpConnectionMetadataOptional.get();
            httpConnectionMetadata.setLastActive(LocalDateTime.now());
            Boolean isLoggedOut = httpConnectionMetadata.getIsLoggedOut();
            if (isLoggedOut) {
                HTTPAlarmMessage httpAlarmMessage = new HTTPAlarmMessage(
                        httpConnectionMetadata.getOrganizationId(),
                        httpConnectionMetadata.getId(),
                        httpConnectionMetadata.getWorkspaceId(),
                        httpConnectionMetadata.getStationId(),
                        "CONNECTED",
                        httpConnectionMetadata.getLastActive()
                );
                alarmMessageKafkaTemplate.send(connectionAlarmTopic, httpAlarmMessage);
                log.info("Send (RE)CONNECTED alarm on connection {}", httpConnectionMetadata.getId());

                httpConnectionMetadata.setIsLoggedOut(false);
            }
            httpConnectionMetadataRepository.save(httpConnectionMetadata);
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


    public void storeHttpConnection(HTTPConnection httpConnection) {
        HTTPConnectionMetadata httpConnectionMetadata = httpConnectionMapper.toHttpConnectionMetadata(httpConnection);
        httpConnectionMetadataRepository.save(httpConnectionMetadata);
    }

    public void deleteHttpConnection(HTTPConnection httpConnection) {
        HTTPConnectionMetadata httpConnectionMetadata = httpConnectionMapper.toHttpConnectionMetadata(httpConnection);
        httpConnectionMetadataRepository.delete(httpConnectionMetadata);
    }


    public void checkConnectionStatus() {
        List<HTTPConnectionMetadata> httpConnectionMetadataList = httpConnectionMetadataRepository.findAll();
        LocalDateTime currentTime = LocalDateTime.now();

        for (HTTPConnectionMetadata httpConnectionMetadata: httpConnectionMetadataList) {

            LocalDateTime lastActiveTime = httpConnectionMetadata.getLastActive();
            int maximumTimeout = httpConnectionMetadata.getMaximumTimeout();
            boolean isLoggedOut = httpConnectionMetadata.getIsLoggedOut();
            boolean active = httpConnectionMetadata.getActive();

            Duration duration = Duration.between(lastActiveTime, currentTime);
            boolean isOverTimeout = Math.toIntExact(duration.toMinutes()) >= maximumTimeout;
            if (isOverTimeout && !isLoggedOut && active) {
                HTTPAlarmMessage httpAlarmMessage = new HTTPAlarmMessage(
                        httpConnectionMetadata.getOrganizationId(),
                        httpConnectionMetadata.getId(),
                        httpConnectionMetadata.getWorkspaceId(),
                        httpConnectionMetadata.getStationId(),
                        "DISCONNECTED",
                        currentTime
                );
                alarmMessageKafkaTemplate.send(connectionAlarmTopic, httpAlarmMessage);
                log.info("Send DISCONNECTED alarm on connection {}", httpConnectionMetadata.getId());

                httpConnectionMetadata.setIsLoggedOut(true);
                httpConnectionMetadataRepository.save(httpConnectionMetadata);
            }
        }
    }
}
