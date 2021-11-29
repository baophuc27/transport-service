package com.reeco.ingestion.cache.service;

import com.reeco.common.model.dto.Alarm;
import com.reeco.ingestion.application.mapper.AlarmMapper;
import com.reeco.ingestion.cache.model.AlarmCache;
import com.reeco.ingestion.domain.ParamAndAlarm;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.AlarmInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
public class AlarmCacheService implements AlarmCacheUseCase {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    AlarmInfoRepository alarmInfoRepository;

    @Autowired
    AlarmMapper alarmMapper;

    private final String ALARM_CACHE = "alarm_cache";

    public void loadDataToCache(){
        alarmInfoRepository
                .findAll()
                .map(v->alarmMapper.toDomain(v))
                .groupBy(Alarm::getParamId)
                .flatMap(
                        gr->gr.collectList()
                                .map(v->new ParamAndAlarm(gr.key(),v))
                )
                .map(v->{
                    put(v);
                    return v;
                }).subscribe(v->log.info("Stored Alarms into cache: {}", v));
    }

    public void put(ParamAndAlarm paramAndAlarm) {
        if (paramAndAlarm != null) {
            // key: paramId in string type, value: list of alarms
           getCache().put(paramAndAlarm.getParamId().toString(), paramAndAlarm);
            log.info("Put Cache: {}", paramAndAlarm);
        }
    }

    public void put(List<AlarmCache> alarmCaches){
        for (AlarmCache alarmCache: alarmCaches) {
            if(alarmCache != null)
                getCache().put(alarmCache.getKey(), alarmCache);
                log.info("Put {} to {}", alarmCache, ALARM_CACHE);
        }
    }

    public ParamAndAlarm get(String key){
        return getCache().get(key, ParamAndAlarm.class);
    }

    @Override
    public void evict(String key) {
        getCache().evict(key);
    }

    @Override
    public void evict(List<AlarmCache> alarmCaches) {
        for (AlarmCache alarmCache: alarmCaches) {
            if(alarmCache != null)
                getCache().evict(alarmCache.getKey());
                log.info("Evict Cache: {}", alarmCache);
        }

    }

    public Cache getCache(){
        return cacheManager.getCache(ALARM_CACHE);
    }


//    @Autowired
//    AlarmInfoRepository alarmInfoRepository;


//    public void update(){
//        Flux<AlarmInfo> alarmInfoFlux = alarmInfoRepository.findAll();
//        List<AlarmInfo> alarmInfoList = alarmInfoFlux.collectList().block();
//        for (AlarmInfo alarmInfo: alarmInfoList){
//            AlarmCache alarmCache = new AlarmCache(alarmInfo);
//            cacheManager.getCache("alarm_cache").put(alarmCache.getKey(),alarmCache);
//        }
//    }

}
