package com.reeco.ingestion.application.service;

import com.reeco.ingestion.application.port.in.IncomingAlarm;
import com.reeco.ingestion.application.port.in.IncomingConfigEvent;
import com.reeco.ingestion.application.port.out.IndicatorRepository;
import com.reeco.ingestion.application.port.out.InsertEventPort;
import com.reeco.ingestion.application.usecase.StoreConfigUseCase;
import com.reeco.ingestion.cache.model.AlarmCache;
import com.reeco.ingestion.cache.service.AlarmCacheUseCase;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.AlarmInfo;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.IndicatorInfo;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.ParamsByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.AlarmInfoRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.IndicatorInfoRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.ParamsByOrgRepository;
import com.reeco.ingestion.utils.annotators.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@UseCase
@RequiredArgsConstructor
@Log4j2
public class StoreConfigService implements StoreConfigUseCase{
    @Autowired
    ParamsByOrgRepository paramsByOrgRepository;

    @Autowired
    AlarmInfoRepository alarmInfoRepository;

    @Autowired
    IndicatorInfoRepository indicatorInfoRepository;


    final CacheManager cacheManager;

    @Autowired
    AlarmCacheUseCase alarmCacheUseCase;



    @Override
    public void storeConfig(IncomingConfigEvent config){
        Mono<IndicatorInfo> indicatorInfo = indicatorInfoRepository.findById(new IndicatorInfo.Key(config.getParameter().getIndicatorId()));
        IndicatorInfo indicatorInfo1 = indicatorInfo.block();

        ParamsByOrg.Key paramsByOrgKey = new ParamsByOrg.Key(config.getOrgId(),config.getParameter().getId());
        ParamsByOrg paramsByOrg = new ParamsByOrg(paramsByOrgKey, indicatorInfo1.getPartitionKey().getIndicatorId(),
                config.getParameter().getEnglishName(),100L,config.getConnectionId(),indicatorInfo1.getStandardUnit());
        List<AlarmInfo> alarmInfoList = new ArrayList<>();
        List<AlarmCache> alarmCaches = new ArrayList<>();
        for (IncomingAlarm alarmInfo: config.getParameter().getAlarms()){
            AlarmInfo.Key alarmInfoKey = new AlarmInfo.Key(config.getOrgId(),config.getParameter().getId(),alarmInfo.getId());
            AlarmInfo alarmInfo1 = new AlarmInfo(alarmInfoKey,alarmInfo.getAlarmType(),alarmInfo.getMinValue(),alarmInfo.getMaxValue(),alarmInfo.getNumOfMatch(),
                    alarmInfo.getMaintainType(),alarmInfo.getFrequence(),alarmInfo.getFrequenceType(), LocalDateTime.now());
            alarmInfoList.add(alarmInfo1);
            alarmCaches.add(new AlarmCache(alarmInfo, config.getOrgId(),config.getParameter().getId()));
        }

        Flux<AlarmInfo> alarmInfoFlux = alarmInfoRepository.saveAll(alarmInfoList);
        alarmInfoFlux.subscribe();
        alarmCacheUseCase.putDatatoCache(alarmCaches);
        paramsByOrgRepository.save(paramsByOrg).subscribe();

        Cache cache = alarmCacheUseCase.getCache();

    }
}
