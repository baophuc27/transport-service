package com.reeco.ingestion.application.port.out;

import java.sql.Timestamp;

public interface NumStatRepository {
    void insert(Timestamp startTime, Timestamp endTime);

}
