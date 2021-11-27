package com.reeco.ingestion.application.service;

import com.reeco.ingestion.application.port.out.IndicatorRepository;
import com.reeco.ingestion.application.usecase.EntityManagementUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class EntityManagementService implements EntityManagementUseCase {

    private final IndicatorRepository indicatorRepository;

    private final
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
