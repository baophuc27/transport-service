package com.reeco.ingestion.application.service;

import com.reeco.ingestion.application.usecase.StatisticEventUseCase;
import com.reeco.ingestion.utils.annotators.UseCase;
import com.reeco.ingestion.application.port.out.NumStatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Log4j2
public class StatisticEventService implements StatisticEventUseCase {

    @Autowired
    private NumStatRepository numStatRepository;


    @Override
    public void updateNumStatEvent(LocalDateTime endTime) {
        numStatRepository.insert(endTime);
    }

    @Override
    public void updateCatStatEvent() {
    }


}
