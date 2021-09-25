package com.reeco.transport.application.service;

import com.reeco.transport.application.port.in.ReceiveFilePort;
import com.reeco.transport.application.port.out.BatchingFilePort;
import com.reeco.transport.application.port.out.StreamingDataPort;
import com.reeco.transport.application.usecase.DataManagementUseCase;
import com.reeco.transport.domain.DataRecord;
import com.reeco.transport.domain.DeviceConnection;
import com.reeco.transport.domain.ServiceConnection;
import com.reeco.transport.utils.annotators.UseCase;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@UseCase
@RequiredArgsConstructor
public class DataManagementService implements DataManagementUseCase {

    private final BatchingFilePort batchingFilePort;

    private final StreamingDataPort streamingDataPort;

    private final ReceiveFilePort receiveFilePort;

    @Override
    public void batchingFile()
    {
    }

    @Override
    public void transferData(DataRecord dataRecord){
        streamingDataPort.streamData(dataRecord);
    }

    @Override
    public void receiveData(DataRecord dataRecord){
        transferData(dataRecord);
    }

}
