package com.reeco.transport.application.usecase;

import com.reeco.transport.infrastructure.model.DeleteAttributeMessage;
import com.reeco.transport.infrastructure.model.DeleteCustomIdMessage;
import com.reeco.transport.infrastructure.model.UpsertAttributeMessage;
import com.reeco.transport.infrastructure.model.UpsertCustomIdMessage;

public interface DeviceManagementUseCase {

    void registerConnection(RegisterDeviceCommand command);

    void deleteConnection(DeleteDeviceCommand command);

    void registerAttribute(UpsertAttributeMessage message);

    void deleteAttribute(DeleteAttributeMessage message);

    void upsertCustomId(UpsertCustomIdMessage message);

    void deleteCustomId(DeleteCustomIdMessage message);
    void registerDevice(RegisterDeviceCommand command);

    void deleteDevice(DeleteDeviceCommand command);
}