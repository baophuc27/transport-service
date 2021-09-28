package com.reeco.ingestion.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.ingestion.infrastructure.kafka.ActionType;
import com.reeco.ingestion.infrastructure.kafka.EntityType;
import com.reeco.ingestion.infrastructure.model.DeleteAttributeMessage;
import com.reeco.ingestion.infrastructure.model.DeleteConnectionMessage;
import com.reeco.ingestion.infrastructure.model.UpsertAttributeMessage;
import com.reeco.ingestion.application.mapper.ConnectionMapper;
import com.reeco.ingestion.application.usecase.DeleteDeviceCommand;
import com.reeco.ingestion.application.usecase.DeviceManagementUseCase;
import com.reeco.ingestion.application.usecase.RegisterDeviceCommand;
import com.reeco.ingestion.infrastructure.model.UpsertConnectionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class KafkaMessageSubscriber {

    @Autowired
    private final DeviceManagementUseCase deviceManagementUseCase;

    private ObjectMapper objectMapper;

    @Autowired
    private ConnectionMapper connectionMapper;

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

//    @KafkaListener(topicPartitions = @TopicPartition(
//            topic = "reeco_core_backend",
//            partitionOffsets = { @PartitionOffset(
//                    partition = "0",
//                    initialOffset = "7") }),containerFactory = "connectionListener")
    @KafkaListener(topics = "reeco_core_backend",containerFactory = "connectionListener")
    public void process(@Headers Map<String,byte[]> header,@Payload ConsumerRecord<String,byte[]> message) {
        EntityType entityType = EntityType
                                .valueOf((new String(header.get("entityType")))
                                .replace("\"",""));
        ActionType actionType = ActionType
                                .valueOf((new String(header.get("actionType")))
                                .replace("\"",""));

        switch (entityType){
            case CONNECTION:
                switch (actionType){
                    case UPSERT:
                        UpsertConnectionMessage upsertConnectionMessage = parseObject(message.value(),UpsertConnectionMessage.class);
                        assert upsertConnectionMessage != null;
                        log.info("Received message : {}", upsertConnectionMessage.toString());
                        RegisterDeviceCommand registerCommand =  connectionMapper.messageToRegisterCommand(upsertConnectionMessage);
                        deviceManagementUseCase.registerDevice(registerCommand);
                        break;

                    case DELETE:
                        DeleteConnectionMessage deleteConnectionMessage = parseObject(message.value(), DeleteConnectionMessage.class);
                        assert deleteConnectionMessage != null;
                        log.info("Received message: {}",deleteConnectionMessage.toString());
                        DeleteDeviceCommand deleteDeviceCommand = connectionMapper.messageToDeleteCommand(deleteConnectionMessage);
                        deviceManagementUseCase.deleteDevice(deleteDeviceCommand);
                        break;
                    default: break;
                }
                break;
            case ATTRIBUTE:
                switch (actionType){
                    case UPSERT:
                        UpsertAttributeMessage upsertAttributeMessage = parseObject(message.value(),UpsertAttributeMessage.class);
                        assert upsertAttributeMessage != null;
                        log.info("Received message : {}",upsertAttributeMessage.toString());
                        deviceManagementUseCase.registerAttribute(upsertAttributeMessage);
                        break;
                    case DELETE:
                        DeleteAttributeMessage deleteAttributeMessage = parseObject(message.value(),DeleteAttributeMessage.class);
                        assert deleteAttributeMessage != null;
                        log.info("Received message : {}",deleteAttributeMessage.toString());
                        deviceManagementUseCase.deleteAttribute(deleteAttributeMessage);
                        break;
                }
                break;
            case ALARM: break;
            case DEVICE: break;
            case DASHBOARD: break;
            default: break;
        }
    }


}
