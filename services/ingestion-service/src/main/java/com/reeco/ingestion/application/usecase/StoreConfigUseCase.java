package com.reeco.ingestion.application.usecase;

import com.reeco.ingestion.application.port.in.IncomingConfigEvent;

public interface StoreConfigUseCase {
    void storeConfig(IncomingConfigEvent config);
}
