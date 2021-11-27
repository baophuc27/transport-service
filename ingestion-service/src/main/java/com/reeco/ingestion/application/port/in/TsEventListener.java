package com.reeco.ingestion.application.port.in;

public interface TsEventListener {
    void listen(String deviceInfo);
}
