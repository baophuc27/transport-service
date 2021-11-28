package com.reeco.ingestion.cache.service;

import com.reeco.common.model.dto.Parameter;
import com.reeco.ingestion.cache.model.AlarmCache;
import com.reeco.ingestion.domain.ParamAndAlarm;
import org.springframework.cache.Cache;

import java.util.List;

public interface AlarmCacheUseCase {
    void loadDataToCache();

    void put(List<AlarmCache> alarmCaches);

    void put(ParamAndAlarm paramAndAlarm);

    void evict(String key);

    void evict(List<AlarmCache> alarmCaches);

    ParamAndAlarm get(String key);

    Cache getCache();
}
