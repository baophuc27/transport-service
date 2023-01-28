package com.reeco.transport.adapter.out;

import com.reeco.transport.application.port.out.SendAlarmPort;
import com.reeco.transport.infrastructure.KafkaMessageProducer;
import com.reeco.common.model.dto.AlarmMessage;
import com.reeco.transport.utils.annotators.Adapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Adapter
@RequiredArgsConstructor
@Slf4j
public class SendAlarmMessageAdapter implements SendAlarmPort {

    private final KafkaMessageProducer messageProducer;

    @Override
    public void send(AlarmMessage message) {
        log.info("Sending message: {}",message.toString());
//        messageProducer.sendAlarmMessage(message);
    }
}
