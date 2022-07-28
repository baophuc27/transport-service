package com.reeco.transport.application.usecase;

import com.reeco.transport.infrastructure.model.DeleteAttributeMessage;
import com.reeco.transport.infrastructure.model.UpsertAttributeMessage;

public interface DeviceManagementUseCase {

    void registerConnection(RegisterDeviceCommand command);

    void deleteConnection(DeleteDeviceCommand command);

    void registerAttribute(UpsertAttributeMessage message);

    void deleteAttribute(DeleteAttributeMessage message);

    void registerDevice(RegisterDeviceCommand command);

    void deleteDevice(DeleteDeviceCommand command);
}