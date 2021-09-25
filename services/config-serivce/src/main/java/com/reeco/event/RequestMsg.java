package com.reeco.event;

import com.reeco.kafka.MessageHeaders;
import com.reeco.model.ActionType;
import com.reeco.model.EntityType;

import java.io.Serializable;

public interface RequestMsg extends Serializable {

    byte[] toByteArray();

    Long getStationId();

    ActionType getActionType();

    EntityType getEntityType();

    MessageHeaders buildHeaders();
}
