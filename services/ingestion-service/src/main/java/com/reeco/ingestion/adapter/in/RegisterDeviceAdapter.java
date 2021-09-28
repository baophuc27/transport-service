package com.reeco.ingestion.adapter.in;

import com.reeco.ingestion.application.mapper.ConnectionMapper;
import com.reeco.ingestion.application.usecase.RegisterDeviceCommand;
import com.reeco.ingestion.application.port.in.RegisterDevicePort;
import com.reeco.ingestion.utils.annotators.Adapter;
import com.reeco.ingestion.domain.DeviceConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


@Adapter
@RequiredArgsConstructor
@Slf4j
public class RegisterDeviceAdapter implements RegisterDevicePort {

    @Autowired
    private ConnectionMapper connectionMapper;

    @Override
    public DeviceConnection registerDevice(RegisterDeviceCommand registerDeviceCommand){

            DeviceConnection deviceConnection = connectionMapper.registerCommandToFTPDeviceConnection(registerDeviceCommand);
            log.info("Received register request with device id: {}",deviceConnection.getDeviceId());
            return deviceConnection;
    }

}
