package com.reeco.ingestion.application.service;

import com.reeco.ingestion.application.usecase.DeleteDeviceCommand;
import com.reeco.ingestion.application.usecase.DeviceManagementUseCase;
import com.reeco.ingestion.application.usecase.RegisterDeviceCommand;
import com.reeco.ingestion.domain.DeviceConnection;
import com.reeco.ingestion.infrastructure.model.DeleteAttributeMessage;
import com.reeco.ingestion.infrastructure.model.UpsertAttributeMessage;
import com.reeco.ingestion.utils.annotators.UseCase;
import com.reeco.ingestion.utils.exception.EventProcessingException;
import com.reeco.ingestion.application.mapper.ConnectionMapper;
import com.reeco.ingestion.application.port.in.DeleteDevicePort;
import com.reeco.ingestion.application.port.out.BeginReceivingDataPort;
import com.reeco.ingestion.application.port.out.SaveAttributePort;
import com.reeco.ingestion.application.port.out.StoreConfigurationPort;
import com.reeco.ingestion.application.port.in.RegisterDevicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class DeviceManagementService implements DeviceManagementUseCase {

    private final RegisterDevicePort registerDevicePort;

    private final BeginReceivingDataPort beginReceivingDataPort;

    private final StoreConfigurationPort storeConfigurationPort;

    private final DeleteDevicePort deleteDevicePort;

    private final SaveAttributePort saveAttributePort;

    @Autowired
    private ConnectionMapper connectionMapper;

    @Override
    public void registerDevice(RegisterDeviceCommand command){
        DeviceConnection deviceConnection = connectionMapper.registerCommandToFTPDeviceConnection(command);
        try{
            storeConfigurationPort.save(deviceConnection);
        }
        catch (EventProcessingException exception){
            log.warn("Error when saving configuration : {}",exception.getMessage());
        }
        beginReceivingDataPort.beginReceivingData(deviceConnection);
    }

    @Override
    public void deleteDevice(DeleteDeviceCommand command){
        deleteDevicePort.deleteDevice(command);
    }

    @Override
    public void registerAttribute(UpsertAttributeMessage message) {
        saveAttributePort.save(message);
    }

    @Override
    public void deleteAttribute(DeleteAttributeMessage message) {
        saveAttributePort.delete(message);
    }

}
