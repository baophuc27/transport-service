package com.reeco.transport.application.port.out;

import com.reeco.transport.infrastructure.model.DeleteCustomIdMessage;
import com.reeco.transport.infrastructure.model.UpsertCustomIdMessage;

public interface UpdateCustomIdPort {

    void save(UpsertCustomIdMessage message);

    void delete(DeleteCustomIdMessage message);
}
