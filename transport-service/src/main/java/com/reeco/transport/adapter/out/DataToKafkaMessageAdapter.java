package com.reeco.transport.adapter.out;

import com.reeco.transport.utils.annotators.Adapter;
import com.reeco.transport.application.mapper.DataRecordMapper;
import com.reeco.transport.application.port.out.StreamingDataPort;
import com.reeco.transport.domain.DataRecord;
import com.reeco.transport.infrastructure.KafkaMessageProducer;
import com.reeco.transport.infrastructure.model.DataRecordMessage;
import com.reeco.transport.infrastructure.persistence.postgresql.PostgresAttributeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Adapter
@RequiredArgsConstructor
@Slf4j
public class DataToKafkaMessageAdapter implements StreamingDataPort {

    private final KafkaMessageProducer kafkaMessageProducer;

    @Autowired
    private DataRecordMapper mapper;

    @Autowired
    private PostgresAttributeRepository attributeRepository;

    @Override
    public void streamData(DataRecord dataRecord){
        DataRecordMessage message = mapper.domainEntityToMessage(dataRecord);
        String mappedAttribute = attributeRepository.findMappingAttribute(dataRecord.getDeviceId(),dataRecord.getKey());
        if (mappedAttribute != null){
            message.setParameter(mappedAttribute);
            log.info("Sending message: {}",message.toString());
            kafkaMessageProducer.sendDataRecord(message);
        }
    }
}
