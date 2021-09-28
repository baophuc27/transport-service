package com.reeco.ingestion.adapter.out;

import com.reeco.ingestion.application.mapper.DataRecordMapper;
import com.reeco.ingestion.application.port.out.StreamingDataPort;
import com.reeco.ingestion.domain.DataRecord;
import com.reeco.ingestion.infrastructure.KafkaMessageProducer;
import com.reeco.ingestion.infrastructure.model.DataRecordMessage;
import com.reeco.ingestion.infrastructure.persistence.postgresql.PostgresAttributeRepository;
import com.reeco.ingestion.utils.annotators.Adapter;
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
