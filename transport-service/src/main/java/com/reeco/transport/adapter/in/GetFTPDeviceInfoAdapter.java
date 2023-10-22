package com.reeco.transport.adapter.in;

import com.reeco.transport.application.port.in.GetAlarmInfoPort;
import com.reeco.transport.infrastructure.persistence.postgresql.DeviceEntity;
import com.reeco.transport.infrastructure.persistence.postgresql.PostgresDeviceRepository;
import com.reeco.transport.infrastructure.persistence.postgresql.PostgresFTPRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Slf4j
public class GetFTPDeviceInfoAdapter implements GetAlarmInfoPort {
    @Autowired
    private PostgresDeviceRepository postgresDeviceRepository;

    @Autowired
    private PostgresFTPRepository ftpRepository;

    @Override
    public DeviceEntity getDeviceInfo(String ftpUsername,boolean isNeedUpdate) {
        Integer deviceId = ftpRepository.findFtpByUserName(ftpUsername);
        log.info(String.valueOf(isNeedUpdate));
        if (deviceId != null){
            DeviceEntity device = postgresDeviceRepository.findDeviceById(deviceId);
            LocalDateTime oldTime = device.getLastActive();
            if (isNeedUpdate){
                DeviceEntity device_save = device;
                device_save.setLastActive(LocalDateTime.now());
                postgresDeviceRepository.save(device_save);
            }
            device.setLastActive(oldTime);
            return device;
        }
        return null;
    }

    @Override
    public void updateDeviceLogout(Integer deviceId, boolean logged_out){
        LocalDateTime now = LocalDateTime.now();
        postgresDeviceRepository.updateDeviceLoggedOut(deviceId,logged_out,now);
        log.info("Update log_out status of device: {} to {}",deviceId,logged_out);
    }
}
