package com.reeco.transport.adapter.out;

import com.reeco.transport.application.mapper.CustomIdMapper;
import com.reeco.transport.application.port.out.UpdateCustomIdPort;
import com.reeco.transport.infrastructure.model.DeleteCustomIdMessage;
import com.reeco.transport.infrastructure.model.UpsertCustomIdMessage;
import com.reeco.transport.infrastructure.persistence.postgresql.CustomIdEntity;
import com.reeco.transport.infrastructure.persistence.postgresql.PostgresCustomIdsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CustomIdAdapter implements UpdateCustomIdPort {

    @Autowired
    private PostgresCustomIdsRepository customIdsRepository;

    @Autowired
    private CustomIdMapper mapper;

    @Override
    public void save(UpsertCustomIdMessage message) {
        CustomIdEntity customIdEntity = mapper.messageToEntity(message);
        try {
            customIdsRepository.save(customIdEntity);
            log.info("Saved custom_id entity into database: {}", customIdEntity.toString());
        } catch (RuntimeException ignored){
            log.warn("Error when save new custom id: {}",ignored.getMessage());
        }
    }

    @Override
    public void delete(DeleteCustomIdMessage message) {
        try{
            String customIdType = message.customIdType;
            Integer originalId = message.originalId;
            customIdsRepository.deleteCustomIdEntity(customIdType,originalId);
            log.info("Deleted custom ID for" + customIdType + "- ID: " + originalId.toString());
        } catch (RuntimeException ignored){
            log.warn("Error when delete new custom id: {}",ignored.getMessage());
        }
    }
}
