package com.reeco.ingestion.cache.service;

import com.reeco.ingestion.cache.model.AlarmCache;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.AlarmInfo;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.AlarmInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.List;

@RequiredArgsConstructor
@Log4j2
public class AlarmCacheService implements AlarmCacheUseCase {

    final CacheManager cacheManager;

    @Autowired
    AlarmInfoRepository alarmInfoRepository;

    public void loadDataToCache(){
        Flux<AlarmInfo> alarmInfoFlux = alarmInfoRepository.findAll();
        List<AlarmInfo> alarmInfoList = alarmInfoFlux.collectList().block();
        for (AlarmInfo alarmInfo: alarmInfoList){
            AlarmCache alarmCache = new AlarmCache(alarmInfo);
            cacheManager.getCache("alarm_cache").put(alarmCache.getKey(),alarmCache);
        }
    }

    public void putDatatoCache(List<AlarmCache> alarmCaches){
        for (AlarmCache alarmCache: alarmCaches) {
            if(alarmCache != null)
                cacheManager.getCache("alarm_cache").put(alarmCache.getKey(), alarmCache);
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
