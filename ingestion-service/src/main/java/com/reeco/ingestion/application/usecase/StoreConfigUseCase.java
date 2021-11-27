package com.reeco.ingestion.application.usecase;


import com.reeco.common.model.dto.Parameter;

public interface StoreConfigUseCase {
    void storeParameter(Parameter parameter);

    void deleteParameter(Parameter parameter);
}
