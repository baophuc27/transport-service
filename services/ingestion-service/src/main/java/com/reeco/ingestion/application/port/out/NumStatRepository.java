package com.reeco.ingestion.application.port.out;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface NumStatRepository {
    void insert(LocalDateTime localDateTime);

}
