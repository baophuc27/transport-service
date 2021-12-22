package com.reeco.ingestion.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.common.model.dto.*;
import com.reeco.common.model.enumtype.ActionType;
import com.reeco.common.model.enumtype.EntityType;
import com.reeco.common.model.enumtype.Protocol;
import com.reeco.ingestion.application.port.in.RuleEngineEvent;
import com.reeco.ingestion.application.usecase.RuleEngineUseCase;
import com.reeco.ingestion.application.usecase.StoreConfigUseCase;
import com.reeco.ingestion.application.usecase.StoreTsEventUseCase;
import com.reeco.ingestion.cache.service.AlarmCacheUseCase;
import com.reeco.ingestion.cache.service.IndicatorCacheUseCase;
import com.reeco.ingestion.cache.service.RuleEngineCacheUseCase;
import com.reeco.ingestion.domain.Indicator;
import com.reeco.ingestion.domain.ParamAndAlarm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class IncomingTsEventController {

    private final StoreTsEventUseCase storeTsEventUseCase;

    private final StoreConfigUseCase storeConfigUseCase;

    private final AlarmCacheUseCase alarmCacheUseCase;

    private final IndicatorCacheUseCase indicatorCacheUseCase;

    private final RuleEngineUseCase ruleEngineUseCase;

    private final RuleEngineCacheUseCase ruleEngineCacheUseCase;

    private static ObjectMapper objectMapper;


    @PostConstruct
    private void postProcess(){
        objectMapper = new ObjectMapper();
    }

    private <T> T parseObject(byte[] message, Class<T> valueType){
        try {
            return objectMapper.readValue(message,valueType);
        } catch (RuntimeException | IOException e) {
            log.warn("Error when parsing message object: {}",e.getMessage());
            return null;
        }
    }

    @KafkaListener(topics = "reeco_time_series_event",containerFactory = "timeSeriesEventListener")
    public void listen(@Payload IncomingTsEvent event){
        try {
            Indicator indicator = indicatorCacheUseCase.get(event.getIndicatorId().toString());
            if (indicator != null) {
                ParamAndAlarm paramAndAlarm = alarmCacheUseCase.get(event.getParamId().toString());
                RuleEngineEvent ruleEngineEvent = null;
                if (paramAndAlarm != null) {
                    for (Alarm alarm : paramAndAlarm.getAlarms()) {
                        ruleEngineEvent = ruleEngineUseCase.handleRuleEvent(alarm, event, indicator);
                        if (ruleEngineEvent.getIsAlarm()) break;
                    }
                    storeTsEventUseCase.storeEvent(ruleEngineEvent, indicator);
                }
            } else log.warn("Indicator Not Found with Id: [{}]", event.getIndicatorId());
        }
        catch (RuntimeException exception){
            exception.printStackTrace();
        }
    }

    @KafkaListener(topics = "reeco_config_event", containerFactory = "configEventListener")
    public void listen(@Headers Map<String, byte[]> header, @Payload String config) {
        try {
            EntityType entityType = EntityType.valueOf(new String(header.get("entityType"), StandardCharsets.UTF_8));
            ActionType actionType = ActionType.valueOf(new String(header.get("actionType"), StandardCharsets.UTF_8));
            log.info("Received entityType: {}, actionType: {}", entityType, actionType);
            switch (entityType) {
                case PARAM:
                    Parameter parameter  = objectMapper.readValue(config, Parameter.class);
                    log.info("Received Param Config: {}", parameter.toString());
                    switch (actionType) {
                        case DELETE: storeConfigUseCase.deleteParameter(parameter); break;
                        case UPSERT: storeConfigUseCase.storeParameter(parameter); break;
                        default: break;
                    }
                case CONNECTION:
                    Protocol protocol = Protocol.valueOf(new String(header.get("protocol"), StandardCharsets.UTF_8));
                    if (protocol.equals(Protocol.HTTP)) {
                        HTTPConnection httpConnection = objectMapper.readValue(config, HTTPConnection.class);
                        switch (actionType) {
                            case UPSERT:
                                storeConfigUseCase.storeConnection(httpConnection);
                                break;
                            case DELETE:
                                storeConfigUseCase.deleteConnection(httpConnection);
                                break;
                            default:
                                break;
                        }
                    }else if(protocol.equals(Protocol.FTP)) {
                        FTPConnection ftpConnection = objectMapper.readValue(config, FTPConnection.class);
                        switch (actionType) {
                            case UPSERT:
                                storeConfigUseCase.storeConnection(ftpConnection);
                                break;
                            case DELETE:
                                storeConfigUseCase.deleteConnection(ftpConnection);
                                break;
                            default:
                                break;
                        }
                    }
                default: break;
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        alarmCacheUseCase.loadDataToCache();
        log.info("Load Alarm to cache manager when start up");
        indicatorCacheUseCase.loadDataToCache();
        log.info("Load Indicator to cache manager when start up");
        ruleEngineCacheUseCase.loadDataToCache();
        log.info("Load Rule Engine to cache manager when start up");
    }
}
