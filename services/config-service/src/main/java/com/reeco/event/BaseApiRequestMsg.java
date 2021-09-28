package com.reeco.event;

import com.reeco.model.ActionType;
import com.reeco.model.EntityType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public abstract class BaseApiRequestMsg implements RequestMsg {

    private Long stationId;

    private long createdTime;

    private ActionType actionType;

    private EntityType entityType;

    public BaseApiRequestMsg(Long stationId,
                             long createdTime,
                             ActionType eventAction,
                             EntityType entityType) {
        this.stationId = stationId;
        this.createdTime = createdTime;
        this.actionType = eventAction;
        this.entityType = entityType;
    }

    public abstract byte[] toByteArray();

}