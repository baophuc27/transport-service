package com.reeco.transport.application.usecase;

public interface AlarmManagementUsecase {
    void alarmConnected(String ftpUsername, String eventTime);

    void alarmDisconnected(String ftpUsername, String eventTime);

    void alarmTimeout(String ftpUsername, String eventTime);

    void alarmAuthenticationFailed(String ftpUsername, String eventTime);
}
