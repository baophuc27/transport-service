package com.reeco.ingestion.application.service;

import com.reeco.ingestion.application.usecase.DataManagementUseCase;
import com.reeco.ingestion.domain.DataRecord;
import com.reeco.ingestion.utils.annotators.UseCase;
import com.reeco.ingestion.application.port.in.ReceiveFilePort;
import com.reeco.ingestion.application.port.out.BatchingFilePort;
import com.reeco.ingestion.application.port.out.StreamingDataPort;
import lombok.RequiredArgsConstructor;

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
