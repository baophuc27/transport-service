package com.reeco.ingestion.application.service;

import com.reeco.ingestion.application.port.out.IndicatorRepository;
import com.reeco.ingestion.application.port.out.ParameterRepository;
import com.reeco.ingestion.application.usecase.EntityManagementUseCase;
import com.reeco.ingestion.utils.annotators.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class EntityManagementService implements EntityManagementUseCase {

    private final IndicatorRepository indicatorRepository;


    @Override
    public void registerAlarm() {

    }

    @Override
    public void registerParam() {

    }

    @Override
    public void deleteAlarm() {

    }

    @Override
    public void deleteParam() {

    }
}
