package com.reeco.transport.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.common.model.dto.DataSharingConnection;
import com.reeco.common.model.enumtype.Protocol;
import com.reeco.transport.application.mapper.ConnectionMapper;
import com.reeco.transport.application.usecase.DeleteDeviceCommand;
import com.reeco.transport.application.usecase.DeviceManagementUseCase;
import com.reeco.transport.application.usecase.RegisterDeviceCommand;
import com.reeco.transport.infrastructure.kafka.ActionType;
//import com.reeco.transport.infrastructure.kafka.EntityType;
import com.reeco.common.model.enumtype.EntityType;
import com.reeco.transport.infrastructure.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import com.reeco.common.model.dto.FTPConnection;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;

import static com.reeco.common.model.enumtype.EntityType.CUSTOMID;

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
//            topic = "reeco_config_event",
//            partitionOffsets = { @PartitionOffset(
//                    partition = "0",
//                    initialOffset = "0") }),containerFactory = "connectionListener")
    @KafkaListener(topics = "reeco_config_event",containerFactory = "connectionListener")
    public void process(@Headers Map<String,byte[]> header,@Payload ConsumerRecord<String,byte[]> message) {
        EntityType entityType = EntityType
                                .valueOf((new String(header.get("entityType")))
                                .replace("\"",""));
        ActionType actionType = ActionType
                                .valueOf((new String(header.get("actionType")))
                                .replace("\"",""));
        String protocol = "FTP";
        try{
            protocol = new String(header.get("protocol"));
        }
        catch (RuntimeException exception){
            log.info("No problem");
        }
        log.info(entityType.name().toString());

        switch (entityType){
            case CONNECTION:
                if (protocol.equals("FTP")){
                    switch (actionType){
                        case UPSERT:
                            FTPConnection upsertConnectionMessage = parseObject(message.value(), FTPConnection.class);
                            if (upsertConnectionMessage.getNotificationType() != ""){
                                upsertConnectionMessage.setNotificationType("NEVER");
                            }
                            assert upsertConnectionMessage != null;
                            log.info("Received message : {}", upsertConnectionMessage.toString());
                            RegisterDeviceCommand registerCommand =  connectionMapper.messageToRegisterCommand(upsertConnectionMessage);
                            log.info("Received message : {}", registerCommand.toString());
                            deviceManagementUseCase.registerConnection(registerCommand);
                            break;

                        case DELETE:
                            DeleteConnectionMessage deleteConnectionMessage = parseObject(message.value(), DeleteConnectionMessage.class);
                            assert deleteConnectionMessage != null;
                            log.info("Received message: {}",deleteConnectionMessage.toString());
                            DeleteDeviceCommand deleteDeviceCommand = connectionMapper.messageToDeleteCommand(deleteConnectionMessage);
                            deviceManagementUseCase.deleteConnection(deleteDeviceCommand);
                            break;
                        default: break;
                    }
                    break;
                } else if (protocol.equals("DATA_SHARING")) {
                    switch (actionType){
                        case UPSERT:
                            FTPConnection upsertConnectionMessage = parseObject(message.value(), FTPConnection.class);
                            if (upsertConnectionMessage.getNotificationType() != ""){
                                upsertConnectionMessage.setNotificationType("NEVER");
                            }
                            assert upsertConnectionMessage != null;
                            log.info("Received message : {}", upsertConnectionMessage.toString());
                            RegisterDeviceCommand registerCommand =  connectionMapper.messageToRegisterCommand(upsertConnectionMessage);
                            log.info("Received message : {}", registerCommand.toString());
                            deviceManagementUseCase.registerDevice(registerCommand);
                            break;
                        case DELETE:
                            DeleteConnectionMessage deleteConnectionMessage = parseObject(message.value(), DeleteConnectionMessage.class);
                            assert deleteConnectionMessage != null;
                            log.info("Received message: {}",deleteConnectionMessage.toString());
                            DeleteDeviceCommand deleteDeviceCommand = connectionMapper.messageToDeleteCommand(deleteConnectionMessage);
                            deviceManagementUseCase.deleteDevice(deleteDeviceCommand);
                            break;
                    }
                }
                break;

            case PARAM:
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
            case CUSTOMID:
                switch (actionType){
                    case UPSERT:
                        UpsertCustomIdMessage upsertCustomIdMessage = parseObject(message.value(), UpsertCustomIdMessage.class);
                        assert upsertCustomIdMessage != null;
                        log.info("Received CUSTOM ID message: {}", upsertCustomIdMessage.toString());
                        deviceManagementUseCase.upsertCustomId(upsertCustomIdMessage);
                        break;
                    case DELETE:
                        DeleteCustomIdMessage deleteCustomIdMessage = parseObject(message.value(),DeleteCustomIdMessage.class);
                        assert deleteCustomIdMessage != null;
                        log.info("Received DELETE CUSTOM_ID message: {}",deleteCustomIdMessage.toString());
                        deviceManagementUseCase.deleteCustomId(deleteCustomIdMessage);
                        break;
                }
            case DEVICE: break;
            case DASHBOARD: break;
            default: break;
        }
    }
}
