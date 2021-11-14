package com.reeco.event;


import com.reeco.kafka.DefaultMsgHeaders;
import com.reeco.kafka.MessageHeaders;
import com.reeco.model.ActionType;
import com.reeco.model.Connection;
import com.reeco.model.EntityType;
import com.reeco.model.Protocol;
import com.reeco.utils.Utils;
import lombok.Getter;
import lombok.Setter;

public class ConnectionRequestMsg extends BaseApiRequestMsg implements Connection {

    @Getter
    @Setter
    private Connection connection;


    public ConnectionRequestMsg(long createdTime,
                                Long orgId,
                                ActionType actionType,
                                EntityType entityType,
                                Connection connection){

        super(orgId, createdTime, actionType, entityType);
        this.connection = connection;
    }

    public byte[] toByteArray(){
        return Utils.getBytes(this);
    }

    @Override
    public Protocol getProtocol() {
        return getConnection().getProtocol();
    }


    @Override
    public MessageHeaders buildHeaders(){
        DefaultMsgHeaders headers = new DefaultMsgHeaders();
        headers.put("actionType", Utils.getBytes(getActionType()));
        headers.put("entityType", Utils.getBytes(getEntityType()));
        headers.put("protocol", Utils.getBytes(getProtocol()));
        return headers;
    }

}
