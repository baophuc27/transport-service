package com.reeco.ingestion.adapter.out;

import com.reeco.ingestion.application.port.out.BeginReceivingDataPort;
import com.reeco.ingestion.domain.DeviceConnection;
import com.reeco.ingestion.infrastructure.FileProcessor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SFTPObserveFileAdapter implements BeginReceivingDataPort {

    private final FileProcessor fileProcessor;

    @Override
    public void beginReceivingData(DeviceConnection deviceConnection){
        String folderName = deviceConnection.getDeviceId();
        fileProcessor.createDirectory(folderName);
        fileProcessor.observe(folderName);
    }
}
