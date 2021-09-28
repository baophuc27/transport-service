package com.reeco.ingestion.application.port.in;

import com.reeco.ingestion.application.usecase.DeleteDeviceCommand;

public interface DeleteDevicePort {
    void deleteDevice(DeleteDeviceCommand command);
}
