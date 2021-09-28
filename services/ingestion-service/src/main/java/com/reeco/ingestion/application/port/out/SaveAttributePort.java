package com.reeco.ingestion.application.port.out;

import com.reeco.ingestion.infrastructure.model.DeleteAttributeMessage;
import com.reeco.ingestion.infrastructure.model.UpsertAttributeMessage;

public interface SaveAttributePort {
    void save(UpsertAttributeMessage message);

    void delete(DeleteAttributeMessage message);
}
