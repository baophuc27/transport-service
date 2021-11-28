package com.reeco.ingestion.application.usecase;

import com.reeco.ingestion.application.port.in.RuleEngineEvent;
import com.reeco.ingestion.domain.Indicator;

public interface StoreTsEventUseCase {

    void storeEvent(RuleEngineEvent event, Indicator indicator);

}
