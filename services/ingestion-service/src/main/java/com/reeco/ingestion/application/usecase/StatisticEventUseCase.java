package com.reeco.ingestion.application.usecase;


import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface StatisticEventUseCase {

    void updateNumStatEvent(Timestamp startTime, Timestamp endTime);

    void updateCatStatEvent();
}
