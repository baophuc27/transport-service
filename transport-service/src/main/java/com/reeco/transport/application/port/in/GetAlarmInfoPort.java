package com.reeco.transport.application.port.in;

import com.reeco.transport.infrastructure.persistence.postgresql.DeviceEntity;

public interface GetAlarmInfoPort {
    DeviceEntity getDeviceInfo(String ftpUsername, boolean isNeedUpdate);

    void updateDeviceLogout(Integer deviceId, boolean logged_out);
}
