package com.reeco.transport.adapter.out;

import com.reeco.transport.application.port.out.BeginReceivingDataPort;
import com.reeco.transport.domain.DeviceConnection;
import com.reeco.transport.infrastructure.FileProcessor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SFTPObserveFileAdapter implements BeginReceivingDataPort {

    private final FileProcessor fileProcessor;

    @Override
    public void beginReceivingData(DeviceConnection deviceConnection){
        String folderName = "/data/"+deviceConnection.getDeviceId();
        fileProcessor.createDirectory(folderName);
        if (deviceConnection.getActive()){
            fileProcessor.observe(folderName);
        }
    }
}
