package com.reeco.event;


import com.reeco.kafka.DefaultMsgHeaders;
import com.reeco.kafka.MessageHeaders;
import com.reeco.model.ActionType;
import com.reeco.model.Attribute;
import com.reeco.model.AttributeType;
import com.reeco.model.EntityType;
import com.reeco.utils.Utils;
import lombok.Getter;

public class ParametersRequestMsg extends BaseApiRequestMsg implements Attribute{

    @Getter
    private final Long connectionId;

    @Getter
    private final Attribute attribute;

    public ParametersRequestMsg(long createdTime,
                                Long stationId,
                                Long connectionId,
                                ActionType eventAction,
                                EntityType entityType,
                                Attribute attribute){

        super(stationId, createdTime, eventAction, entityType);
        this.connectionId = connectionId;
        this.attribute = attribute;
    }

    public byte[] toByteArray(){
        return Utils.getBytes(this);
    }

    @Override
    public AttributeType getParameterType() {
        return attribute.getParameterType();
    }

    @Override
    public MessageHeaders buildHeaders(){
        DefaultMsgHeaders headers = new DefaultMsgHeaders();
        headers.put("actionType", Utils.getBytes(getActionType()));
        headers.put("entityType", Utils.getBytes(getEntityType()));
        headers.put("attributeType", Utils.getBytes(getParameterType()));
        return headers;
    }

}
