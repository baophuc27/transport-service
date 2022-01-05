package com.reeco.transport.adapter.in;

import com.reeco.transport.application.port.in.GetAlarmInfoPort;
import com.reeco.transport.infrastructure.persistence.postgresql.DeviceEntity;
import com.reeco.transport.infrastructure.persistence.postgresql.PostgresDeviceRepository;
import com.reeco.transport.infrastructure.persistence.postgresql.PostgresFTPRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class GetFTPDeviceInfoAdapter implements GetAlarmInfoPort {
    @Autowired
    private PostgresDeviceRepository postgresDeviceRepository;

    @Autowired
    private PostgresFTPRepository ftpRepository;

    @Override
    public DeviceEntity getDeviceInfo(String ftpUsername) {
        Integer deviceId = ftpRepository.findFtpByUserName(ftpUsername);
        if (deviceId != null){
            DeviceEntity device = postgresDeviceRepository.findDeviceById(deviceId);
            return device;
        }
        return null;
    }
}
