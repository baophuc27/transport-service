package com.reeco.ingestion.application.port.in;

import com.reeco.ingestion.domain.DataRecord;

public interface ReceiveFilePort {
    DataRecord receiveFile();
}
