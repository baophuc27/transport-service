package com.reeco.transport.adapter.out;

import com.reeco.transport.application.port.out.MQTTMessageForwardPort;
import com.reeco.transport.domain.DataRecord;

public class MQTTMessageAdapter implements MQTTMessageForwardPort {
    @Override
    public void forward(DataRecord dataRecord) {

    }
}
