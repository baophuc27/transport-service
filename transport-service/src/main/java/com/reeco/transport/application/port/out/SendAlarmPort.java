package com.reeco.transport.application.port.out;

import com.reeco.transport.infrastructure.model.AlarmMessage;

public interface SendAlarmPort {
    void send(AlarmMessage message);
}
