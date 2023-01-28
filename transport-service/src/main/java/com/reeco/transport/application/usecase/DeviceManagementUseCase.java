package com.reeco.transport.application.usecase;

import com.reeco.transport.infrastructure.model.*;

public interface DeviceManagementUseCase {

    void registerConnection(RegisterDeviceCommand command);

    void deleteConnection(DeleteDeviceCommand command);

    void registerAttribute(UpsertAttributeMessage message);

    void deleteAttribute(DeleteAttributeMessage message);

    void upsertCustomId(UpsertCustomIdMessage message);

    void deleteCustomId(DeleteCustomIdMessage message);
    void registerDevice(RegisterDeviceCommand command);

    void deleteDevice(DeleteDeviceCommand command);

    void upsertApiKey(UpsertApiKeyMessage message);

    void deleteApiKey(DeleteApiKeyMessage message);

    void upsertMQTT(UpsertMQTTMessage message);

    void deleteMQTT(DeleteMQTTMessage message);
}