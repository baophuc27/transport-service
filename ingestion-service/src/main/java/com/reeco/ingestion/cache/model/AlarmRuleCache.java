package com.reeco.ingestion.cache.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
public class AlarmRuleCache {
    private Long alarmId;

    private LocalDateTime lastMatchedTime;

    private Long matchedCount;
}
