package com.reeco.transport.application.port.in;

import com.reeco.transport.application.usecase.DeleteDeviceCommand;

public interface DeleteDevicePort {
    void deleteDevice(DeleteDeviceCommand command);
}
