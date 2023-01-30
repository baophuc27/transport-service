package com.reeco.transport.application.usecase;


import com.reeco.transport.domain.DataRecord;

public interface DataManagementUseCase {
    void batchingFile();

    void transferData(DataRecord dataRecord);

    void receiveData(DataRecord dataRecord, boolean isSyncData);

    void forwardMQTTMessage(DataRecord dataRecord);
}
