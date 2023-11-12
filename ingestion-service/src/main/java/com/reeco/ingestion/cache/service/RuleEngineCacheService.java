package com.reeco.ingestion.cache.service;

import com.reeco.common.model.dto.Alarm;
import com.reeco.ingestion.application.mapper.AlarmMapper;
import com.reeco.ingestion.cache.model.AlarmCache;
import com.reeco.ingestion.cache.model.AlarmRuleCache;
import com.reeco.ingestion.domain.Indicator;
import com.reeco.ingestion.domain.ParamAndAlarm;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.AlarmInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
public class RuleEngineCacheService implements RuleEngineCacheUseCase {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    AlarmMapper alarmMapper;

    @Autowired
    AlarmInfoRepository alarmInfoRepository;

    private final String ALARM_RULE_CACHE = "alarm_rule_cache";
    public void loadDataToCache(){
        alarmInfoRepository
                .findAll()
                .map(v->{
                    AlarmRuleCache alarmRuleCache = new AlarmRuleCache(v.getPartitionKey().getAlarmId(),
                            LocalDateTime.now(), 0L, false);
                    put(alarmRuleCache);
                    return alarmRuleCache;
                }).subscribe();
    }

    public void put(AlarmRuleCache ruleCache) {
        getCache().put(ruleCache.getAlarmId().toString(), ruleCache);
        log.info("Put {} to {}", ruleCache, ALARM_RULE_CACHE);
    }

    public void put(List<AlarmRuleCache> ruleCaches) {
        for (AlarmRuleCache ruleCache: ruleCaches){
            put(ruleCache);
        }
    }

    public AlarmRuleCache get(String key){
        return getCache().get(key, AlarmRuleCache.class);
    }

    @Override
    public void evict(String key) {
        getCache().evict(key);
        log.info("Evict key: {} from {}",key, ALARM_RULE_CACHE );
    }

    @Override
    public void evict(List<String> keys) {
        for (String k: keys){
            evict(k);
        }
    }

    public Cache getCache(){
        return cacheManager.getCache(ALARM_RULE_CACHE);
    }

}
