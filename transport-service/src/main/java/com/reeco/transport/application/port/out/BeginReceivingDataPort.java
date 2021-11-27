package com.reeco.transport.application.port.out;

import com.reeco.transport.domain.DeviceConnection;

public interface BeginReceivingDataPort {
    void beginReceivingData(DeviceConnection deviceConnection);
}
