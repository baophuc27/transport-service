package com.reeco.transport.infrastructure;

import com.reeco.transport.domain.DeviceConnection;

import java.io.IOException;

public abstract class MainStorage {
    public abstract void createDirectory(DeviceConnection device) throws IOException;
    public abstract void addFile(String directory);
    public abstract void readFile(String filepath);
    public abstract void deleteFile(String filepath);
}
