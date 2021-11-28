package com.reeco.ingestion.cache.service;

import com.reeco.ingestion.application.mapper.IndicatorMapper;
import com.reeco.ingestion.cache.model.AlarmCache;
import com.reeco.ingestion.domain.Indicator;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.IndicatorInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
public class IndicatorCacheService implements IndicatorCacheUseCase {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    IndicatorMapper indicatorMapper;

    @Autowired
    IndicatorInfoRepository indicatorInfoRepository;

    private final String INDICATOR_CACHE = "indicator_cache";

    public void loadDataToCache(){
        indicatorInfoRepository
                .findAll()
                .map(v->{
                    Indicator indicator = indicatorMapper.toDomain(v);
                    getCache().put(indicator.getIndicatorId().toString(), indicator);
                    return indicator;
                }).subscribe(v->log.info("Stored Indicator into cache: {}", v));
    }

    public void putDataToCache(List<Indicator> indicators){
        for (Indicator indicator: indicators) {
            if(indicator != null)
                getCache().put(indicator.getIndicatorId(), indicator);
                log.info("Put {} to {}", indicator, INDICATOR_CACHE);
        }
    }

    @Override
    public Indicator get(String key) {
        return getCache().get(key, Indicator.class);
    }


    public Cache getCache(){
        return cacheManager.getCache(INDICATOR_CACHE);
    }



}
