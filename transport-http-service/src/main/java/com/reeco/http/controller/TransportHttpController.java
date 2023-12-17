package com.reeco.http.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.common.model.dto.HTTPConnection;
import com.reeco.common.model.dto.Parameter;
import com.reeco.common.model.enumtype.ActionType;
import com.reeco.common.model.enumtype.EntityType;
import com.reeco.common.model.enumtype.Protocol;
import com.reeco.common.model.enumtype.TransportType;
import com.reeco.common.utils.AES;
import com.reeco.http.cache.ConnectionCache;
import com.reeco.http.model.RequestDto;
import com.reeco.http.model.Connection;
import com.reeco.http.model.ParameterCache;
import com.reeco.http.infrastructure.persistence.cassandra.entity.ConnectionByOrg;
import com.reeco.http.infrastructure.persistence.cassandra.entity.ParamsByOrg;
import com.reeco.http.infrastructure.persistence.cassandra.repository.ConnectionByOrgRepository;
import com.reeco.http.service.TransportHttpService;
import com.reeco.http.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(value = {"*"})
@RestController()
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/reeco")
public class TransportHttpController {

    @Autowired
    ConnectionCache connectionCache;

    @Autowired
    ConnectionByOrgRepository connectionByOrgRepository;
    @Autowired
    TransportHttpService transportHttpService;

    @PostMapping("/receive-data")
    public ResponseEntity<ApiResponse> receiveData(@RequestHeader("access_key") String accessKey, @RequestHeader("connection_id") String id , @RequestBody RequestDto requestDto) throws Exception{
        String key = id+"%"+accessKey;
        ApiResponse apiResponse = transportHttpService.pushDataToKafka(requestDto, accessKey, id);
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

    @KafkaListener(topics = "reeco_config_event", containerFactory = "configEventListener")
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
                                    parameter.getId()
                            ),
                            parameter.getIndicatorId(),
                            parameter.getEnglishName(),
                            parameter.getStationId(),
                            parameter.getConnectionId(),
                            parameter.getUnit(),
                            parameter.getKeyName()
                    );
                    ParameterCache parameterCache = new ParameterCache(paramsByOrg);
                    Optional<ConnectionByOrg> conc = connectionByOrgRepository.getByIdAndTransportType(parameter.getConnectionId(), TransportType.HTTP);
                    String key = conc.get().getPartitionKey().getConnectionId() + "%" +conc.get().getAccessToken();
                    switch (actionType) {
                        case UPSERT:

                            if (!conc.isPresent()){
                                log.error("Connection not exists!");
                                throw new Exception("Connection not exists!");
                            }
                            connectionCache.put(parameterCache, key);
                            log.info("Saved to cache: " + parameterCache);
                            break;
                        case DELETE:
                            connectionCache.evict(parameterCache, key);
                            log.info("Evict paramId from cache: " + parameterCache.getParamId());
                            break;
                        default:
                            break;

                    }
                    break;
                case CONNECTION:
                    Protocol protocol = Protocol.valueOf(new String(header.get("protocol"), StandardCharsets.UTF_8));
                    if (protocol.equals(Protocol.HTTP)) {
                        HTTPConnection httpConnection = new ObjectMapper().readValue(config, HTTPConnection.class);
                        if (httpConnection.getAccessToken() != null) {
                            httpConnection.setAccessToken(AES.decrypt(httpConnection.getAccessToken()));
                        }
                        ConnectionByOrg connectionByOrg = new ConnectionByOrg(
                                new ConnectionByOrg.Key(
                                        httpConnection.getOrganizationId(),
                                        httpConnection.getId()
                                ),
                                httpConnection.getAccessToken(),
                                httpConnection.getEnglishName(),
                                TransportType.HTTP
                        );
                        Connection connection = new Connection(connectionByOrg);
                        switch (actionType){
                            case UPSERT:
                                connectionCache.put(connection);
                                transportHttpService.storeHttpConnection(httpConnection);
                                log.info("Saved to cache: " + connection);
                                break;

                            case DELETE:
                                connectionCache.evict(connection.getConnectionId() +"%"+ connection.getAccessToken());
                                transportHttpService.deleteHttpConnection(httpConnection);
                                log.info("Evict connectionId cache: " + connection.getConnectionId());
                                break;
                            default:
                                break;
                        }

                    }
                    break;
                default: break;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void checkConnectionStatus() {
        log.info("Checking connection status");
        transportHttpService.checkConnectionStatus();
    }


}
