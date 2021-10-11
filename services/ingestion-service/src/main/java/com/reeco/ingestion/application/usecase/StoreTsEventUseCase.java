package com.reeco.ingestion.application.usecase;


import com.reeco.ingestion.application.port.in.IncomingTsEvent;
import com.reeco.ingestion.domain.NumericTsEvent;

public interface StoreTsEventUseCase {

    void storeEvent(IncomingTsEvent event);
}
