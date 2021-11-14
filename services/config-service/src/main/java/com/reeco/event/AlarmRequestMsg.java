package com.reeco.event;


import com.reeco.kafka.DefaultMsgHeaders;
import com.reeco.kafka.MessageHeaders;
import com.reeco.model.ActionType;
import com.reeco.model.AlarmType;
import com.reeco.model.EntityType;
import com.reeco.model.ParamType;
import com.reeco.model.dto.AlarmDTO;
import com.reeco.utils.Utils;
import lombok.Getter;

public class AlarmRequestMsg extends BaseApiRequestMsg {

    @Getter
    private final Long paramId;

    @Getter
    private final AlarmDTO alarmDTO;

    public AlarmRequestMsg(long createdTime,
                           Long orgId,
                           Long paramId,
                           ActionType eventAction,
                           EntityType entityType,
                           AlarmDTO alarmDTO){

        super(orgId, createdTime, eventAction, entityType);
        this.paramId = paramId;
        this.alarmDTO = alarmDTO;
    }

    public byte[] toByteArray(){
        return Utils.getBytes(this);
    }

    public AlarmType getAlarmType() {
        return alarmDTO.getAlarmType();
    }

    @Override
    public MessageHeaders buildHeaders(){
        DefaultMsgHeaders headers = new DefaultMsgHeaders();
        headers.put("actionType", Utils.getBytes(getActionType()));
        headers.put("entityType", Utils.getBytes(getEntityType()));
        headers.put("alarmType", Utils.getBytes(getAlarmType()));
        return headers;
    }

}
