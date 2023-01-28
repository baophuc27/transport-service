package com.reeco.transport.application.port.out;

import com.reeco.transport.infrastructure.model.DeleteApiKeyMessage;
import com.reeco.transport.infrastructure.model.UpsertApiKeyMessage;

public interface UpdateApiKeyPort {
    void save(UpsertApiKeyMessage message);

    void delete(DeleteApiKeyMessage message);
}
