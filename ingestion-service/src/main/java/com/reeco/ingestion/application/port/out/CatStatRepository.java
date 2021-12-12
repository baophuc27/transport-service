package com.reeco.ingestion.application.port.out;

import java.sql.Timestamp;

public interface CatStatRepository {
    void insertCat(Timestamp startTime, Timestamp endTime);
}