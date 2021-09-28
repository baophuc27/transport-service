package com.reeco.ingestion.application.port.out;

import com.reeco.ingestion.domain.DeviceConnection;
import com.reeco.ingestion.utils.exception.EventProcessingException;

public interface StoreConfigurationPort {
    void save(DeviceConnection deviceConnection) throws EventProcessingException;
}
