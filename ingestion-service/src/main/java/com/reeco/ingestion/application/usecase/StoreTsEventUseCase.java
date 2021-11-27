package com.reeco.ingestion.application.usecase;


import com.reeco.ingestion.application.port.in.IncomingTsEvent;

public interface StoreTsEventUseCase {

    void storeEvent(IncomingTsEvent event);

}
