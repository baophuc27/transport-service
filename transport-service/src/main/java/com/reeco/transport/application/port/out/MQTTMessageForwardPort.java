package com.reeco.transport.application.port.out;

import com.reeco.transport.domain.DataRecord;

public interface MQTTMessageForwardPort {
    void forward(DataRecord dataRecord);
}
