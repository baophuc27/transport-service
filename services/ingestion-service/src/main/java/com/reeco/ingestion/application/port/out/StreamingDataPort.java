package com.reeco.ingestion.application.port.out;

import com.reeco.ingestion.domain.DataRecord;

public interface StreamingDataPort {
    void streamData(DataRecord dataRecord);
}
