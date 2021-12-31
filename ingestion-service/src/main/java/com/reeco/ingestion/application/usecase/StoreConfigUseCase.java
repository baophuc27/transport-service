package com.reeco.ingestion.application.usecase;


import com.reeco.common.model.dto.Connection;
import com.reeco.common.model.dto.FTPConnection;
import com.reeco.common.model.dto.HTTPConnection;
import com.reeco.common.model.dto.Parameter;

public interface StoreConfigUseCase {
    void storeParameter(Parameter parameter);

    void deleteParameter(Parameter parameter);

    void storeConnection(Connection connection);

    void deleteConnection(Connection connection);

    void storeConnection(FTPConnection ftpConnection);

}
