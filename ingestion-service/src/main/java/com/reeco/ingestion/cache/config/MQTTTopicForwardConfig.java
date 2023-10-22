package com.reeco.ingestion.cache.config;

import com.reeco.ingestion.infrastructure.persistence.postgres.entity.MQTTConfigEntity;
import com.reeco.ingestion.infrastructure.persistence.postgres.repository.PostgresMQTTConfigRepository;
import com.reeco.ingestion.utils.annotators.Infrastructure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Infrastructure
public class MQTTTopicForwardConfig {

    @Autowired
    PostgresMQTTConfigRepository mqttConfigRepository;

    static Map<Integer, Set<String>> TOPIC_CONNECTION_FORWARD;

    static Map<Integer, Set<String>> TOPIC_PARAMETER_FORWARD;

    static Map<Integer, Set<String>> TOPIC_ORGANIZATION_FORWARD;

    @Scheduled(fixedDelay = 60000)
    private void updateMQTTConfig(){
        List<MQTTConfigEntity> allConfig = mqttConfigRepository.getAllEntities();

        for (MQTTConfigEntity config : allConfig){
            log.info("MQTT Config: ",config.toString());
        }
    }
}
