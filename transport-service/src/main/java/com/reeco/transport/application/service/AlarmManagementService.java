package com.reeco.transport.application.service;

import com.reeco.transport.application.mapper.AlarmMapper;
import com.reeco.transport.application.port.in.GetAlarmInfoPort;
import com.reeco.transport.application.port.out.SendAlarmPort;
import com.reeco.transport.application.usecase.AlarmManagementUsecase;
import com.reeco.common.model.dto.AlarmMessage;
import com.reeco.transport.infrastructure.persistence.postgresql.DeviceEntity;
import com.reeco.transport.utils.annotators.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class AlarmManagementService implements AlarmManagementUsecase {

    private final GetAlarmInfoPort getAlarmInfoPort;

    private final SendAlarmPort sendAlarmPort;

    @Autowired
    private AlarmMapper mapper;

    @Override
    public void alarmConnected(String ftpUsername, String eventTime,String ipAddress) {
        DeviceEntity device = getAlarmInfoPort.getDeviceInfo(ftpUsername,true);

        if (device != null){
            LocalDateTime now = LocalDateTime.now();
            Integer timeout = device.getMaximumTimeout();
            LocalDateTime activeRange = now.minusMinutes(timeout);

            if (device.getLastActive().isBefore(activeRange)){
                String message = "CONNECTED";
                AlarmMessage alarmMessage = mapper.fromDeviceEntity(device,message,eventTime,ipAddress);
                sendAlarmPort.send(alarmMessage);
                getAlarmInfoPort.updateDeviceLogout(device.getId(),false);
            }
            else{
                log.info("[ALARM] Connected not alarm with active range: "+activeRange+ " less than: "+timeout);
//                log.info(String.valueOf(!device.getNotificationType().equals("NEVER")));
            }
        }
    }

    @Override
    public void alarmDisconnected(DeviceEntity device) {
        if (device != null){
            LocalDateTime now = LocalDateTime.now();
            String message = "DISCONNECTED";
            AlarmMessage alarmMessage = mapper.fromDeviceEntity(device,message, String.valueOf(now),null);
            sendAlarmPort.send(alarmMessage);
            getAlarmInfoPort.updateDeviceLogout(device.getId(),true);
        }

    }

    @Override
    public void alarmTimeout(String ftpUsername, String eventTime) {

    }

    @Override
    public void alarmAuthenticationFailed(String ftpUsername, String eventTime, String ipAddress) {
        DeviceEntity device = getAlarmInfoPort.getDeviceInfo(ftpUsername,false);
        if (device != null){
            String message = "AUTHENTICATION FAILED";
            AlarmMessage alarmMessage = mapper.fromDeviceEntity(device,message,eventTime,ipAddress);
            log.info("[ALARM] Send message: ");
            sendAlarmPort.send(alarmMessage);
        }

    }
}
