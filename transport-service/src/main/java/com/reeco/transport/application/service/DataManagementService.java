package com.reeco.transport.application.service;

import com.reeco.transport.application.port.in.GetAlarmInfoPort;
import com.reeco.transport.application.port.in.ReceiveFilePort;
import com.reeco.transport.application.usecase.DataManagementUseCase;
import com.reeco.transport.domain.DataRecord;
import com.reeco.transport.utils.annotators.UseCase;
import com.reeco.transport.application.port.out.BatchingFilePort;
import com.reeco.transport.application.port.out.StreamingDataPort;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class DataManagementService implements DataManagementUseCase {

    private final BatchingFilePort batchingFilePort;

    private final StreamingDataPort streamingDataPort;

    private final ReceiveFilePort receiveFilePort;

    private final GetAlarmInfoPort getAlarmInfoPort;

    @Override
    public void batchingFile()
    {
    }

    @Override
    public void transferData(DataRecord dataRecord){
        streamingDataPort.streamData(dataRecord);
    }



    @Override
    public void receiveData(DataRecord dataRecord,boolean isSyncData){
        transferData(dataRecord);
    }

    @Override
    public void forwardMQTTMessage(DataRecord dataRecord) {

    }

    @Override
    public void updateDeviceActiveStatus(Integer deviceId) {
        getAlarmInfoPort.updateDeviceLogout(deviceId,false);
    }

}
