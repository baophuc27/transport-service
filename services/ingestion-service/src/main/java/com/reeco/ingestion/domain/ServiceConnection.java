package com.reeco.ingestion.domain;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.*;


public class ServiceConnection {

    private static ServiceConnection serviceConnection;

    private ServiceConnection(){
        this.connections = new HashMap<>();
        this.lastUpdate = LocalDateTime.now();
    }

    @Getter
    @Setter
    private Map<String,DeviceConnection> connections;

    @Getter
    @Setter
    private LocalDateTime lastUpdate;

    private ServiceConnection(Map<String,DeviceConnection> connections){
        this.connections = connections;
    }

    public static ServiceConnection getInstance(){
        if(serviceConnection == null){
            serviceConnection = new ServiceConnection();
        }
        return serviceConnection;

    }



    public void insert(DeviceConnection deviceConnection){
        this.connections.put(deviceConnection.getDeviceId(),deviceConnection);
    }

    public void delete(String deviceId){
        this.connections.remove(deviceId);
    }
    public DeviceConnection getDeviceConnection(String deviceId){
        return this.connections.get(deviceId);
    }

    @Override
    public String toString() {
        return "ServiceConnection{}";
    }
}
