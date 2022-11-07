package com.reeco.transport.application.usecase;

import com.reeco.transport.infrastructure.persistence.postgresql.DeviceEntity;

public interface AlarmManagementUsecase {
    void alarmConnected(String ftpUsername, String eventTime, String ipAddress);

    void alarmDisconnected(DeviceEntity device);


    void alarmTimeout(String ftpUsername, String eventTime);

    void alarmAuthenticationFailed(String ftpUsername, String eventTime, String ipAddress);
}
