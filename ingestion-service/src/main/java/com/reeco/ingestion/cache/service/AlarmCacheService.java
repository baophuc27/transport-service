package com.reeco.ingestion.cache.service;

import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.AlarmInfo;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.AlarmInfoRepository;
import com.reeco.ingestion.cache.model.AlarmCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import reactor.core.publisher.Flux;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
public class AlarmCacheService implements AlarmCacheUseCase {

    final CacheManager cacheManager;

    @Autowired
    AlarmInfoRepository alarmInfoRepository;

    public void loadDataToCache(){
        alarmInfoRepository
                .findAll()
                .map(v->{
                    AlarmCache alarmCache = new AlarmCache(v);
                    cacheManager.getCache("alarm_cache").put(alarmCache.getKey(),alarmCache);
                    return alarmCache;
                }).subscribe(v->log.info("Stored Alarm into cache: {}", v));
    }

    public void putDataToCache(List<AlarmCache> alarmCaches){
        for (AlarmCache alarmCache: alarmCaches) {
            if(alarmCache != null)
                cacheManager.getCache("alarm_cache").put(alarmCache.getKey(), alarmCache);
        }
    }

    @Override
    public void evictDataFromCache(List<AlarmCache> alarmCaches) {
        for (AlarmCache alarmCache: alarmCaches) {
            if(alarmCache != null)
                cacheManager.getCache("alarm_cache").evict(alarmCache.getKey());
        }

    }

    public Cache getCache(){
        return cacheManager.getCache("alarm_cache");
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
