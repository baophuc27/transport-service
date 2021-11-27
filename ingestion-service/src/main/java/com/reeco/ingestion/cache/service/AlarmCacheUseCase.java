package com.reeco.ingestion.cache.service;

import com.reeco.ingestion.cache.model.AlarmCache;
import org.springframework.cache.Cache;

import java.util.List;

public interface AlarmCacheUseCase {
    void loadDataToCache();

    void putDatatoCache(List<AlarmCache> alarmCaches);

    Cache getCache();
}
