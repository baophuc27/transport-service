package com.reeco.transport.application.service;

import com.reeco.transport.application.mapper.ConnectionMapper;
import com.reeco.transport.application.port.in.DeleteDevicePort;
import com.reeco.transport.application.port.out.BeginReceivingDataPort;
import com.reeco.transport.application.port.out.SaveAttributePort;
import com.reeco.transport.application.port.out.StoreConfigurationPort;
import com.reeco.transport.application.usecase.DeleteDeviceCommand;
import com.reeco.transport.application.usecase.GetDeviceConfigurationQuery;
import com.reeco.transport.application.usecase.RegisterDeviceCommand;
import com.reeco.transport.application.usecase.DeviceManagementUseCase;
import com.reeco.transport.application.port.in.RegisterDevicePort;
import com.reeco.transport.domain.DeviceConnection;
import com.reeco.transport.domain.ServiceConnection;
import com.reeco.transport.infrastructure.FileProcessor;
import com.reeco.transport.infrastructure.model.DeleteAttributeMessage;
import com.reeco.transport.infrastructure.model.UpsertAttributeMessage;
import com.reeco.transport.utils.annotators.UseCase;
import com.reeco.transport.utils.exception.EventProcessingException;
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
