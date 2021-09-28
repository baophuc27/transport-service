package com.reeco.ingestion.application.usecase;

import com.reeco.ingestion.infrastructure.model.DeleteAttributeMessage;
import com.reeco.ingestion.infrastructure.model.UpsertAttributeMessage;

public interface DeviceManagementUseCase {

    void registerDevice(RegisterDeviceCommand command);

    void deleteDevice(DeleteDeviceCommand command);

    void registerAttribute(UpsertAttributeMessage message);

    void deleteAttribute(DeleteAttributeMessage message);
}