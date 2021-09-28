package com.reeco.ingestion.application.port.out;

import com.reeco.ingestion.domain.DeviceConnection;

public interface BeginReceivingDataPort {
    void beginReceivingData(DeviceConnection deviceConnection);
}
