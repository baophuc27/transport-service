package com.reeco.transport.application.port.out;

import com.reeco.transport.infrastructure.model.DeleteAttributeMessage;
import com.reeco.transport.infrastructure.model.UpsertAttributeMessage;

public interface SaveAttributePort {
    void save(UpsertAttributeMessage message);

    void delete(DeleteAttributeMessage message);
}
