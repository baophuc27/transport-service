package com.reeco.ingestion.application.usecase;


import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface StatisticEventUseCase {

    void updateNumStatEvent(LocalDateTime endTime);

    void updateCatStatEvent(Timestamp startTime, Timestamp endTime);
}
