package com.reeco.transport.infrastructure;

import com.reeco.transport.infrastructure.persistence.postgresql.MQTTConfigEntity;
import com.reeco.transport.infrastructure.persistence.postgresql.PostgresMQTTConfigRepository;
import com.reeco.transport.utils.annotators.Infrastructure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.io.*;
import java.util.stream.*;

@Slf4j
@Infrastructure
public class MQTTTopicConfig {

    @Autowired
    PostgresMQTTConfigRepository mqttConfigRepository;

    static Map<Integer, Set<String>> TOPIC_CONNECTION_FORWARD = new HashMap<>();

    static Map<Integer, Set<String>> TOPIC_PARAMETER_FORWARD = new HashMap<>();

    static Map<Integer, Set<String>> TOPIC_ORGANIZATION_FORWARD = new HashMap<>();

    @Scheduled(fixedDelay = 60000)
    private void updateMQTTConfig(){
        TOPIC_ORGANIZATION_FORWARD = new HashMap<>();
        TOPIC_PARAMETER_FORWARD = new HashMap<>();
        TOPIC_CONNECTION_FORWARD = new HashMap<>();
        List<MQTTConfigEntity> allConfig = mqttConfigRepository.getAllEntities();
        for (MQTTConfigEntity config : allConfig){
            log.info("MQTT Config: {}",config);
            addTopicsToNodes(config.getTopic(),config.getConnectionIds(),"CONNECTION");
            addTopicsToNodes(config.getTopic(),config.getParameterIds(),"PARAMETER");
            addTopicsToNodes(config.getTopic(),config.getOrganizationIds(),"ORGANIZATION");
        }
        log.info("\nMQTT Connection Topic");
        for (Integer name: TOPIC_CONNECTION_FORWARD.keySet()) {
            String key = name.toString();
            String value = TOPIC_CONNECTION_FORWARD.get(name).toString();
            System.out.println(key + " " + value);
        }
        log.info("\nMQTT Parameter Topic");
        for (Integer name: TOPIC_PARAMETER_FORWARD.keySet()) {
            String key = name.toString();
            String value = TOPIC_PARAMETER_FORWARD.get(name).toString();
            System.out.println(key + " " + value);
        }
        log.info("\nMQTT Organization Topic");
        for (Integer name: TOPIC_ORGANIZATION_FORWARD.keySet()) {
            String key = name.toString();
            String value = TOPIC_ORGANIZATION_FORWARD.get(name).toString();
            System.out.println(key + " " + value);
        }
    }

    public Set<String> getTopicById(Integer organizationId, Integer connectionId, Integer parameterId){
        Set<String> organizationTopics = TOPIC_ORGANIZATION_FORWARD.get(organizationId);
        Set<String> connectionTopics = TOPIC_CONNECTION_FORWARD.get(connectionId);
        Set<String> parameterTopics = TOPIC_PARAMETER_FORWARD.get(parameterId);

        Set<String> result =  new HashSet<>();
        if (connectionTopics != null){
            result.addAll(connectionTopics);
        }
        if (organizationTopics != null){
            result.addAll(organizationTopics);
        }
        if (parameterTopics != null){
            result.addAll(parameterTopics);
        }
        return result;
    }
    private void addTopicsToNodes(String topic, String ids, String enumType){
        String[] idList = ids.split(",");
        if (enumType.equals("CONNECTION")){
            for (String id : idList){
                Integer newId = Integer.parseInt(id);
                Set<String> set = TOPIC_CONNECTION_FORWARD.get(newId);
                if (set == null) {
                    set = new HashSet<>();
                    TOPIC_CONNECTION_FORWARD.put(newId, set);
                }
                TOPIC_CONNECTION_FORWARD.get(newId).add(topic);
            }
        }
        if (enumType.equals("PARAMETER")){
            for (String id : idList){
                Integer newId = Integer.parseInt(id);
                Set<String> set = TOPIC_PARAMETER_FORWARD.get(newId);
                if (set == null) {
                    set = new HashSet<>();
                    TOPIC_PARAMETER_FORWARD.put(newId, set);
                }
                TOPIC_PARAMETER_FORWARD.get(newId).add(topic);
            }
        }
        if (enumType.equals("ORGANIZATION")){
            for (String id : idList){
                Integer newId = Integer.parseInt(id);
                Set<String> set = TOPIC_ORGANIZATION_FORWARD.get(newId);
                if (set == null) {
                    set = new HashSet<>();
                    TOPIC_ORGANIZATION_FORWARD.put(newId, set);
                }
                TOPIC_ORGANIZATION_FORWARD.get(newId).add(topic);
            }
        }
    }
}
