package com.reeco.ingestion.adapter.out;

import com.reeco.ingestion.application.port.out.StoreConfigurationPort;
import com.reeco.ingestion.domain.DeviceConnection;
import com.reeco.ingestion.domain.ServiceConnection;

public class InMemoryConfigurationAdapter implements StoreConfigurationPort {

    @Override
    public void save(DeviceConnection deviceConnection){
        ServiceConnection serviceConnection = ServiceConnection.getInstance();
        serviceConnection.insert(deviceConnection);
    }
}
