package com.reeco.ingestion.infrastructure.persistence.stimulate;

import com.reeco.ingestion.application.repository.FtpRepository;
import com.reeco.ingestion.domain.DeviceConnection;
import com.reeco.ingestion.domain.ServiceConnection;
import com.reeco.ingestion.domain.protocol.FTPConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class InMemFtpRepository implements FtpRepository {

    private ServiceConnection serviceConnection = ServiceConnection.getInstance();

    @Override
    public FTPConfiguration findByUserName(String userName) {
        Map<String, DeviceConnection> deviceConnections = serviceConnection.getConnections();
        for (Map.Entry<String,DeviceConnection> entry : deviceConnections.entrySet()){
            DeviceConnection connection = entry.getValue();
            if (connection.getProtocolConfiguration().getUserName().equals(userName)){
                return connection.getProtocolConfiguration();
            }
        }
        return null;
    }

    @Override
    public void save(@NotNull FTPConfiguration ftpConfiguration) {

    }

    @Override
    public void insert(FTPConfiguration ftpConfiguration) {

    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public void deleteByUserName(String userName) {

    }

    @Override
    public String[] getAllUserName() {
        String[] userNames = new String[1];
        userNames[0] = "phuc";
        return userNames;
    }
}
