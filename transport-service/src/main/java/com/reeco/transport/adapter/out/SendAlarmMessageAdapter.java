package com.reeco.transport.adapter.out;

import com.reeco.transport.application.mapper.DataRecordMapper;
import com.reeco.transport.application.port.out.SendAlarmPort;
import com.reeco.transport.domain.DataRecord;
import com.reeco.transport.infrastructure.KafkaMessageProducer;
import com.reeco.transport.infrastructure.model.AlarmMessage;
import com.reeco.transport.infrastructure.model.DataRecordMessage;
import com.reeco.transport.infrastructure.persistence.postgresql.AttributeEntity;
import com.reeco.transport.infrastructure.persistence.postgresql.PostgresAttributeRepository;
import com.reeco.transport.utils.annotators.Adapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Adapter
@RequiredArgsConstructor
@Slf4j
public class SendAlarmMessageAdapter implements SendAlarmPort {

    private final KafkaMessageProducer messageProducer;

    @Override
    public void send(AlarmMessage message) {
        log.info("Sending message: {}",message.toString());
        messageProducer.sendAlarmMessage(message);
    }
}
