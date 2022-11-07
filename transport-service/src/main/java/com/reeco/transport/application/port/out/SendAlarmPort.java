package com.reeco.transport.application.port.out;

import com.reeco.common.model.dto.AlarmMessage;

public interface SendAlarmPort {
    void send(AlarmMessage message);
}
