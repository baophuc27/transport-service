package com.reeco.http.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.common.model.dto.HTTPConnection;
import com.reeco.common.model.dto.Parameter;
import com.reeco.common.model.enumtype.ActionType;
import com.reeco.common.model.enumtype.EntityType;
import com.reeco.common.model.enumtype.Protocol;
import com.reeco.common.utils.AES;
import com.reeco.http.cache.ConnectionCache;
import com.reeco.http.model.dto.RequestDto;
import com.reeco.http.model.dto.Connection;
import com.reeco.http.model.dto.ParameterCache;
import com.reeco.http.model.entity.ConnectionByOrg;
import com.reeco.http.model.entity.ParamsByOrg;
import com.reeco.http.service.TransportHttpService;
import com.reeco.http.until.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Map;

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

//    private final static ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/receive-data")
    public ResponseEntity<ApiResponse> receiveData(@RequestHeader("access_key") String accessKey, @RequestBody RequestDto requestDto) throws Exception{
        ApiResponse apiResponse = transportHttpService.pushDataToKafka(requestDto, accessKey);
        return new ResponseEntity<>(apiResponse,apiResponse.getStatus());
    }

    @GetMapping("/access-token")
    public ResponseEntity<ApiResponse> getAccessToken(@RequestParam("connectionId") Long connectionId) throws Exception{
        ApiResponse apiResponse = transportHttpService.getAccessToken(connectionId);
        return new ResponseEntity<>(apiResponse,apiResponse.getStatus());
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        connectionCache.loadDataToCache();
        log.info("Load Connection to cache manager when start up");
    }

//    @KafkaListener(topics = "reeco_config_event", containerFactory = "configEventListener")
    @KafkaListener(id = "1", topicPartitions = @TopicPartition(topic = "reeco_config_event", partitions = {"0", "1"}), containerFactory = "configEventListener")
    public void listen(@Headers Map<String, byte[]> header, @Payload String config) {
        try {
            EntityType entityType = EntityType.valueOf(new String(header.get("entityType"), StandardCharsets.UTF_8));
            ActionType actionType = ActionType.valueOf(new String(header.get("actionType"), StandardCharsets.UTF_8));
            log.info("Received entityType: {}, actionType: {}", entityType, actionType);

            switch (entityType) {
                case PARAM:
                    Parameter parameter = new ObjectMapper().readValue(config, Parameter.class);
                    ParamsByOrg paramsByOrg = new ParamsByOrg(
                            new ParamsByOrg.Key(
                                    parameter.getOrganizationId(),
                                    4L
                            ),
                            parameter.getIndicatorId(),
                            parameter.getEnglishName(),
                            parameter.getStationId(),
                            parameter.getConnectionId(),
                            parameter.getUnit()
                    );
                    ParameterCache parameterCache = new ParameterCache(paramsByOrg);
                    connectionCache.put(parameterCache);
                    log.info("Saved to cache: " + parameterCache);
                    break;
                case CONNECTION:
                    Protocol protocol = Protocol.valueOf(new String(header.get("protocol"), StandardCharsets.UTF_8));
                    if (protocol.equals(Protocol.HTTP)) {
                        HTTPConnection httpConnection = new ObjectMapper().readValue(config, HTTPConnection.class);
                        httpConnection.setAccessToken(AES.decrypt(httpConnection.getAccessToken()));
                        ConnectionByOrg connectionByOrg = new ConnectionByOrg(
                                new ConnectionByOrg.Key(
                                        httpConnection.getOrganizationId(),
                                        Long.valueOf(httpConnection.getAccessToken().split("%")[0])
                                ),
                                httpConnection.getAccessToken(),
                                httpConnection.getEnglishName(),
                                httpConnection.getTransportType()
                        );
                        Connection connection = new Connection(connectionByOrg);
                        connectionCache.put(connection);
                        log.info("Saved to cache: " + connection);
                    }
                    break;
                default: break;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
