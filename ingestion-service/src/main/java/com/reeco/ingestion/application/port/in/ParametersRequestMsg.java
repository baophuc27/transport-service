package com.reeco.ingestion.application.port.in;


import com.reeco.kafka.DefaultMsgHeaders;
import com.reeco.kafka.MessageHeaders;
import com.reeco.common.model.enumtype.ActionType;
import com.reeco.common.model.enumtype.EntityType;
import lombok.Getter;

public class ParametersRequestMsg {

    @Getter
    private final Long organizationId;

    @Getter
    private final Long connectionId;

    @Getter
    private final ParameterDTO parameter;

    private long createdTime;

    private ActionType actionType;

    private EntityType entityType;

    public ParametersRequestMsg(Long connectionId,
                                ParameterDTO parameter,
                                long createdTime,
                                ActionType actionType,
                                EntityType entityType) {
        this.connectionId = connectionId;
        this.parameter = parameter;
        this.createdTime = createdTime;
        this.actionType = actionType;
        this.entityType = entityType;
    }


}
