package com.reeco.ingestion.application.service;

import com.reeco.common.model.dto.AlarmMessage;
import com.reeco.common.model.enumtype.Protocol;
import com.reeco.ingestion.application.mapper.AlarmMapper;
import com.reeco.ingestion.application.usecase.ManageAlarmMessageUseCase;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.ConnectionAlarmInfo;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.AlarmInfoRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.ConnectionAlarmInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor
@Log4j2
public class ManageAlarmMessageService implements ManageAlarmMessageUseCase {

    @Autowired
    ConnectionAlarmInfoRepository connectionAlarmInfoRepository;

    @Autowired
    AlarmMapper alarmMapper;

    @Override
    public void storeAlarmMessage(AlarmMessage alarmMessage){
        Protocol protocol = Protocol.FTP;
        String description = "";
        if (alarmMessage.getIpAddress() != null){
            description = alarmMessage.getIpAddress();
        }
        ConnectionAlarmInfo connectionAlarmInfo = alarmMapper.fromAlarmMessage(alarmMessage,description,protocol);

        connectionAlarmInfoRepository.save(connectionAlarmInfo).subscribe(v-> log.info("Save connection alarm message {}",v));
    }

}
