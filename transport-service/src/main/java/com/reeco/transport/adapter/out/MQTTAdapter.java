package com.reeco.transport.adapter.out;

import com.reeco.transport.application.mapper.ApiKeyMapper;
import com.reeco.transport.application.port.out.UpdateMQTTPort;
import com.reeco.transport.infrastructure.model.DeleteMQTTMessage;
import com.reeco.transport.infrastructure.model.UpsertMQTTMessage;
import com.reeco.transport.infrastructure.persistence.postgresql.MQTTConfigEntity;
import com.reeco.transport.infrastructure.persistence.postgresql.PostgresMQTTConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class MQTTAdapter implements UpdateMQTTPort {

    @Autowired
    PostgresMQTTConfigRepository mqttConfigRepository;

    @Autowired
    private ApiKeyMapper mapper;

    @Override
    public void save(UpsertMQTTMessage message) {
        MQTTConfigEntity mqttConfigEntity = mapper.mqttMessageToEntity(message);
        try {
            mqttConfigRepository.save(mqttConfigEntity);
            log.info("Saved mqtt entity into database: {}",mqttConfigEntity.toString());
        } catch (RuntimeException ignored){
            log.warn("Error when save new mqtt key: {}",ignored.getMessage());
        }
    }

    @Override
    public void delete(DeleteMQTTMessage message) {
        String id = message.getId();
        try {
            mqttConfigRepository.deleteMQTTConfigEntityById(id);
            log.info("Deleted mqtt entity into database: {}",message.toString());
        } catch (RuntimeException ignored){
            log.warn("Error when delete new mqtt key: {}",ignored.getMessage());
        }
    }
}
