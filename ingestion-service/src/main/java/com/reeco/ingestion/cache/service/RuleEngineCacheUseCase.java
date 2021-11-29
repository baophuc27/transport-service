package com.reeco.ingestion.cache.service;

import com.reeco.ingestion.cache.model.AlarmRuleCache;
import com.reeco.ingestion.domain.Indicator;
import org.springframework.cache.Cache;

import java.util.List;

public interface RuleEngineCacheUseCase {
    void loadDataToCache();

    void put(AlarmRuleCache alarmRuleCache);

    void put(List<AlarmRuleCache> alarmRuleCaches);

    void evict(String key);

    void evict(List<String> keys);

    AlarmRuleCache get(String key);

    Cache getCache();
}
