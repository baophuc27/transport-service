package com.reeco.ingestion.application.usecase;


import java.time.LocalDateTime;

public interface StatisticEventUseCase {

    void updateNumStatEvent(LocalDateTime endTime);

    void updateCatStatEvent();
}
