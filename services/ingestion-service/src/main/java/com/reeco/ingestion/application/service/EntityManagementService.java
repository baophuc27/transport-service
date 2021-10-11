package com.reeco.ingestion.application.service;

import com.reeco.ingestion.application.port.out.DeviceRepository;
import com.reeco.ingestion.application.port.out.MetricRepository;
import com.reeco.ingestion.application.usecase.DeleteDeviceCommand;
import com.reeco.ingestion.application.usecase.EntityManagementUseCase;
import com.reeco.ingestion.application.usecase.RegisterDeviceCommand;
import com.reeco.ingestion.domain.Device;
import com.reeco.ingestion.infrastructure.model.DeleteAttributeMessage;
import com.reeco.ingestion.infrastructure.model.UpsertAttributeMessage;
import com.reeco.ingestion.utils.annotators.UseCase;
import com.reeco.ingestion.utils.exception.EventProcessingException;
import com.reeco.ingestion.application.mapper.DeviceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class EntityManagementService implements EntityManagementUseCase {

    private final DeviceRepository deviceRepository;

    private final MetricRepository metricRepository;


    @Autowired
    private DeviceMapper connectionMapper;

    @Override
    public void registerDevice(RegisterDeviceCommand command){
        Device deviceConnection = connectionMapper.registerCommandToFTPDeviceConnection(command);
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
