package com.reeco.transport.application.service;

import com.reeco.transport.application.mapper.AlarmMapper;
import com.reeco.transport.application.port.in.GetAlarmInfoPort;
import com.reeco.transport.application.port.out.SendAlarmPort;
import com.reeco.transport.application.usecase.AlarmManagementUsecase;
import com.reeco.transport.infrastructure.model.AlarmMessage;
import com.reeco.transport.infrastructure.persistence.postgresql.DeviceEntity;
import com.reeco.transport.utils.annotators.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class AlarmManagementService implements AlarmManagementUsecase {

    private final GetAlarmInfoPort getAlarmInfoPort;

    private final SendAlarmPort sendAlarmPort;

    @Autowired
    private AlarmMapper mapper;

    @Override
    public void alarmConnected(String ftpUsername, String eventTime) {
        DeviceEntity device = getAlarmInfoPort.getDeviceInfo(ftpUsername);
        if (device != null){
            String message = "CONNECTED";
            AlarmMessage alarmMessage = mapper.fromDeviceEntity(device,message,eventTime);
            sendAlarmPort.send(alarmMessage);
        }
    }

    @Override
    public void alarmDisconnected(String ftpUsername, String eventTime) {
        DeviceEntity device = getAlarmInfoPort.getDeviceInfo(ftpUsername);
        if (device != null){
            String message = "DISCONNECTED";
            AlarmMessage alarmMessage = mapper.fromDeviceEntity(device,message,eventTime);
            sendAlarmPort.send(alarmMessage);
        }

    }

    @Override
    public void alarmTimeout(String ftpUsername, String eventTime) {

    }

    @Override
    public void alarmAuthenticationFailed(String ftpUsername, String eventTime) {
        DeviceEntity device = getAlarmInfoPort.getDeviceInfo(ftpUsername);
        if (device != null){
            String message = "AUTHENTICATION FAILED";
            AlarmMessage alarmMessage = mapper.fromDeviceEntity(device,message,eventTime);
            sendAlarmPort.send(alarmMessage);
        }

    }
}
