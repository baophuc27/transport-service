package com.reeco.ingestion.application.service;

import com.reeco.common.model.dto.Alarm;
import com.reeco.common.model.dto.Parameter;
import com.reeco.ingestion.application.mapper.AlarmMapper;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.ParamsByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.IndicatorInfoRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.ParamsByOrgRepository;
import com.reeco.ingestion.application.usecase.StoreConfigUseCase;
import com.reeco.ingestion.cache.model.AlarmCache;
import com.reeco.ingestion.cache.service.AlarmCacheUseCase;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.AlarmInfo;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.AlarmInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//@UseCase
@RequiredArgsConstructor
@Log4j2
public class StoreConfigService implements StoreConfigUseCase{
    @Autowired
    ParamsByOrgRepository paramsByOrgRepository;

    @Autowired
    AlarmInfoRepository alarmInfoRepository;

    @Autowired
    AlarmMapper alarmMapper;

    @Autowired
    IndicatorInfoRepository indicatorInfoRepository;


    final CacheManager cacheManager;

    @Autowired
    AlarmCacheUseCase alarmCacheUseCase;


    @Override
    public void storeParameter(Parameter parameter){
        ParamsByOrg.Key paramsByOrgKey = new ParamsByOrg.Key(parameter.getOrganizationId(),parameter.getId());
        ParamsByOrg paramsByOrg = new ParamsByOrg(paramsByOrgKey, parameter.getIndicatorId(),
                parameter.getEnglishName(),100L,parameter.getConnectionId(),parameter.getUnit());
        List<AlarmInfo> alarmInfoList = new ArrayList<>();
        List<AlarmCache> alarmCaches = new ArrayList<>();

        for (Alarm alarm: parameter.getAlarms()){
            AlarmInfo alarmInfo = alarmMapper.toPersistence(alarm);
            alarmInfo.setUpdatedAt(LocalDateTime.now());
            alarmInfoList.add(alarmInfo);
            alarmCaches.add(new AlarmCache(alarm, parameter.getOrganizationId(),parameter.getId()));
        }

        alarmInfoRepository.saveAll(alarmInfoList).subscribe(log::info);
        alarmCacheUseCase.putDataToCache(alarmCaches);
        paramsByOrgRepository.save(paramsByOrg).subscribe(log::info);

        Cache cache = alarmCacheUseCase.getCache();

    }

    @Override
    public void deleteParameter(Parameter parameter) {
        ParamsByOrg.Key paramsByOrgKey = new ParamsByOrg.Key(parameter.getOrganizationId(),parameter.getId());
        List<AlarmInfo> alarmInfo =  alarmInfoRepository
                .findByOrgAndParam(parameter.getOrganizationId(), parameter.getId()).collectList().block();

        List<AlarmCache> alarmCaches = alarmInfo.stream().map(v->new AlarmCache(alarmMapper.toDomain(v),
                v.getPartitionKey().getOrganizationId(),
                v.getPartitionKey().getParamId())).collect(Collectors.toList());

        alarmInfoRepository.deleteAll(alarmInfo).subscribe();
        alarmCacheUseCase.evictDataFromCache(alarmCaches);
        paramsByOrgRepository.deleteById(paramsByOrgKey).subscribe();

    }
}
