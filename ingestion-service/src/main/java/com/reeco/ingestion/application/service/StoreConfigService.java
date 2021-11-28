package com.reeco.ingestion.application.service;

import com.reeco.common.model.dto.Alarm;
import com.reeco.common.model.dto.Parameter;
import com.reeco.ingestion.application.mapper.AlarmMapper;
import com.reeco.ingestion.application.mapper.ParameterMapper;
import com.reeco.ingestion.application.usecase.StoreConfigUseCase;
import com.reeco.ingestion.cache.model.AlarmRuleCache;
import com.reeco.ingestion.cache.service.AlarmCacheUseCase;
import com.reeco.ingestion.cache.service.RuleEngineCacheUseCase;
import com.reeco.ingestion.domain.ParamAndAlarm;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.AlarmInfo;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.ParamsByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.AlarmInfoRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.IndicatorInfoRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.ParamsByOrgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    ParameterMapper parameterMapper;


    @Autowired
    IndicatorInfoRepository indicatorInfoRepository;

    @Autowired
    AlarmCacheUseCase alarmCacheUseCase;

    @Autowired
    RuleEngineCacheUseCase ruleEngineCacheUseCase;

    @Override
    public void storeParameter(Parameter parameter){
        ParamsByOrg paramsByOrg = parameterMapper.toPersistence(parameter);

        List<Alarm> alarms = parameter
                .getAlarms()
                .stream()
                .peek(v->{
                    v.setOrganizationId(parameter.getOrganizationId());
                    v.setParamId(parameter.getId());
                    v.setIndicatorId(parameter.getIndicatorId());
                }).collect(Collectors.toList());

        List<AlarmInfo> alarmInfoList = alarms
                .stream()
                .map(v->{
                    AlarmInfo alarmInfo = alarmMapper.toPersistence(v);
                    alarmInfo.setUpdatedAt(LocalDateTime.now());
                    return alarmInfo;
                }).collect(Collectors.toList());

        List<AlarmRuleCache> alarmRuleCaches =  alarms
                .stream()
                .map(v->new AlarmRuleCache(v.getId(), LocalDateTime.now(), 0L))
                .collect(Collectors.toList());

        alarmInfoRepository.saveAll(alarmInfoList).subscribe(v -> log.info("Saved alarms: {}", v));
        alarmCacheUseCase.put(new ParamAndAlarm(parameter.getId(), alarms));


        ruleEngineCacheUseCase.put(alarmRuleCaches);
        paramsByOrgRepository.save(paramsByOrg).subscribe(v -> log.info("Saved param: {}", v));
    }

    @Override
    public void deleteParameter(Parameter parameter) {
        ParamsByOrg.Key paramsByOrgKey = new ParamsByOrg.Key(parameter.getOrganizationId(),parameter.getId());
        List<AlarmInfo> alarmInfo =  alarmInfoRepository
                .findByOrgAndParam(parameter.getOrganizationId(), parameter.getId())
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Alarm not found with: {}", parameter.toString());
                    return Mono.empty();}))
                .collectList().block();

        if (alarmInfo != null) {
            if (alarmInfo.size() > 0) {
                alarmInfoRepository.deleteAll(alarmInfo).subscribe();
                log.info("Deleted alarms: {}", alarmInfo.toString());
                paramsByOrgRepository.deleteById(paramsByOrgKey).subscribe();
                log.info("Deleted param: {}", paramsByOrgKey.toString());
                List<String> evictKeys = alarmInfo.stream().map(v->v.getPartitionKey().getAlarmId().toString()).collect(Collectors.toList());
                ruleEngineCacheUseCase.evict(evictKeys);
            }
            alarmCacheUseCase.evict(parameter.getId().toString());
            log.info("Evict Alarms From Cache: [{}]", parameter.getId().toString());
        }
    }
}
