package com.reeco.transport.adapter.out;

import com.reeco.transport.application.mapper.ApiKeyMapper;
import com.reeco.transport.application.port.out.UpdateApiKeyPort;
import com.reeco.transport.infrastructure.model.DeleteApiKeyMessage;
import com.reeco.transport.infrastructure.model.UpsertApiKeyMessage;
import com.reeco.transport.infrastructure.persistence.postgresql.ApiKeyEntity;
import com.reeco.transport.infrastructure.persistence.postgresql.PostgresApiKeyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ApiKeyAdapter implements UpdateApiKeyPort {

    @Autowired
    private PostgresApiKeyRepository apiKeyRepository;

    @Autowired
    private ApiKeyMapper mapper;

    @Override
    public void save(UpsertApiKeyMessage message) {
        ApiKeyEntity apiKeyEntity = mapper.messageToEntity(message);
        try {
            apiKeyRepository.save(apiKeyEntity);
            log.info("Saved api_key entity into database: {}",apiKeyEntity.toString());
        } catch (RuntimeException ignored){
            log.warn("Error when save new api key: {}",ignored.getMessage());
        }
    }

    @Override
    public void delete(DeleteApiKeyMessage message) {
        Integer id = message.getId();
        try {
            apiKeyRepository.deleteApiKeyEntityById(id);
            log.info("Deleted api_key entity into database: {}",message.toString());
        } catch (RuntimeException ignored){
            log.warn("Error when delete new api key: {}",ignored.getMessage());
        }
    }
}
