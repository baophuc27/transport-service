package com.reeco.transport.adapter.out;

import com.reeco.transport.application.port.out.StoreConfigurationPort;
import com.reeco.transport.domain.DeviceConnection;
import com.reeco.transport.domain.ServiceConnection;

public class InMemoryConfigurationAdapter implements StoreConfigurationPort {

    @Override
    public void save(DeviceConnection deviceConnection){
        ServiceConnection serviceConnection = ServiceConnection.getInstance();
        serviceConnection.insert(deviceConnection);
    }
}
