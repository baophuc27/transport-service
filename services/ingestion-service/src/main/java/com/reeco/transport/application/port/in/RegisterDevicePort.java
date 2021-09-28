package com.reeco.transport.application.port.in;

import com.reeco.transport.application.usecase.RegisterDeviceCommand;
import com.reeco.transport.domain.DeviceConnection;

public interface RegisterDevicePort {
    /*
    * Once received request from device management service.
    * follow this flow to register a new device:
    *       1. Receive and validate configuration from device management service.
    *       2. Allocate new folder as transport service as well as HDFS.
    * */
    DeviceConnection registerDevice(RegisterDeviceCommand registerDeviceCommand);
}
