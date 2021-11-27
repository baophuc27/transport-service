package com.reeco.ingestion.cache.service;

import com.reeco.ingestion.cache.model.AlarmCache;
import org.springframework.cache.Cache;

import java.util.List;

public interface AlarmCacheUseCase {
    void loadDataToCache();

    void putDataToCache(List<AlarmCache> alarmCaches);

    void evictDataFromCache(List<AlarmCache> alarmCaches);

    Cache getCache();
}
