package com.reeco.transport.application.port.out;

import com.reeco.transport.infrastructure.model.DeleteMQTTMessage;
import com.reeco.transport.infrastructure.model.UpsertMQTTMessage;

public interface UpdateMQTTPort {

    void save(UpsertMQTTMessage message);

    void delete(DeleteMQTTMessage message);
}
