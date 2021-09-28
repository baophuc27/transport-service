package com.reeco.ingestion.application.usecase;


import com.reeco.ingestion.domain.DataRecord;

public interface DataManagementUseCase {
    void batchingFile();

    void transferData(DataRecord dataRecord);

    void receiveData(DataRecord dataRecord);
}
