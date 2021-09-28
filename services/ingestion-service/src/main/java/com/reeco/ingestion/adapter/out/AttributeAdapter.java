package com.reeco.ingestion.adapter.out;

import com.reeco.ingestion.infrastructure.model.DeleteAttributeMessage;
import com.reeco.ingestion.infrastructure.model.UpsertAttributeMessage;
import com.reeco.ingestion.infrastructure.persistence.postgresql.AttributeEntity;
import com.reeco.ingestion.infrastructure.persistence.postgresql.PostgresAttributeRepository;
import com.reeco.ingestion.application.mapper.AttributeMapper;
import com.reeco.ingestion.application.port.out.SaveAttributePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class AttributeAdapter implements SaveAttributePort {

    @Autowired
    private PostgresAttributeRepository attributeRepository;

    @Autowired
    private AttributeMapper attributeMapper;

    @Override
    public void save(UpsertAttributeMessage message) {
        AttributeEntity attributeEntity = attributeMapper.messageToAttributeEntity(message);
        log.info("Entity : {}",attributeEntity.toString());
        try{
            attributeRepository.save(attributeEntity);
        }
        catch (RuntimeException ignored){
            log.warn("Error when store new attribute: {}",ignored.getMessage());
        }
    }

    @Override
    public void delete(DeleteAttributeMessage message) {
        try{
            attributeRepository.deleteById(message.getAttribute().getId());
        }
        catch (RuntimeException exception){
            log.warn("Error when delete attribute {}: {}",message.getStationId(),exception.getMessage());
        }
    }
}
