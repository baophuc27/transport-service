package com.reeco.ingestion.application.usecase;


import com.reeco.common.model.dto.HTTPConnection;
import com.reeco.common.model.dto.Parameter;

public interface StoreConfigUseCase {
    void storeParameter(Parameter parameter);

    void deleteParameter(Parameter parameter);

    void storeConnection(HTTPConnection httpConnection);

    void deleteConnection(HTTPConnection httpConnection);
}
