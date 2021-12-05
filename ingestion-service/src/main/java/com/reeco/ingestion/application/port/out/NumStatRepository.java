package com.reeco.ingestion.application.port.out;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface NumStatRepository {
    void insert(LocalDateTime endTime);

}
