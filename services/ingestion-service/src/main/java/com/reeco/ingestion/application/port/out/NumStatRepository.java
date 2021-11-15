package com.reeco.ingestion.application.port.out;

import jnr.posix.Times;
import org.jetbrains.annotations.NotNull;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface NumStatRepository {
    void insert(Timestamp startTime, Timestamp endTime);

}
