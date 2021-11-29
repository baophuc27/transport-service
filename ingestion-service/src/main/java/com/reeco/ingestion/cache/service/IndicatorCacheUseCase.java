package com.reeco.ingestion.cache.service;

import com.reeco.ingestion.cache.model.AlarmCache;
import com.reeco.ingestion.domain.Indicator;
import org.springframework.cache.Cache;

import java.util.List;

public interface IndicatorCacheUseCase {
    void loadDataToCache();

    void putDataToCache(List<Indicator> indicators);

    Indicator get(String key);

    Cache getCache();
}
