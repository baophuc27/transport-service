package com.reeco.transport.application.port.out;

import com.reeco.transport.domain.DeviceConnection;
import com.reeco.transport.utils.exception.EventProcessingException;

public interface StoreConfigurationPort {
    void save(DeviceConnection deviceConnection) throws EventProcessingException;

    void saveDevice(DeviceConnection deviceConnection) throws EventProcessingException;
}
