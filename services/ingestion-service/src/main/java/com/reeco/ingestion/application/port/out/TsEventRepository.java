package com.reeco.ingestion.application.port.out;

import com.reeco.ingestion.domain.NumericTsEvent;
import com.reeco.ingestion.domain.TextTsEvent;

public interface TsEventRepository {
    void insertNumericEvent(NumericTsEvent e);
    void insertTextEvent(TextTsEvent e);
}
