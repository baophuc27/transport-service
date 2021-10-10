package com.reeco.ingestion.application.usecase;


import com.reeco.ingestion.domain.NumericTsEvent;

public interface StoreTsEventUseCase {

    void storeEvent(NumericTsEvent event);
}
