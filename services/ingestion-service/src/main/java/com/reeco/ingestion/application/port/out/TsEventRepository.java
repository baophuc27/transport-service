package com.reeco.ingestion.application.port.out;

import com.reeco.ingestion.domain.NumericalTsEvent;
import com.reeco.ingestion.domain.CategoricalTsEvent;

public interface TsEventRepository {
    void insertNumericEvent(NumericalTsEvent e);

    void insertCategoricalEvent(CategoricalTsEvent e);

}
