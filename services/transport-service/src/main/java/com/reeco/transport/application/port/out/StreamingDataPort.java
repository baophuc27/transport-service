package com.reeco.transport.application.port.out;

import com.reeco.transport.domain.DataRecord;

public interface StreamingDataPort {
    void streamData(DataRecord dataRecord);
}
