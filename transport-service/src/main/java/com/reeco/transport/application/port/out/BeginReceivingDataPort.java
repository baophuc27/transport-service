package com.reeco.transport.application.port.out;

import com.reeco.transport.domain.DeviceConnection;
import com.reeco.transport.utils.annotators.Infrastructure;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public interface BeginReceivingDataPort {
    void beginReceivingData(DeviceConnection deviceConnection);
}
