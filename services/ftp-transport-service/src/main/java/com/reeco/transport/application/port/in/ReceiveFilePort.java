package com.reeco.transport.application.port.in;

import com.reeco.transport.domain.DataRecord;

public interface ReceiveFilePort {
    DataRecord receiveFile();
}
