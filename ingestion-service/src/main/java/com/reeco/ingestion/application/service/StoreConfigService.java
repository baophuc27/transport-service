package com.reeco.ingestion.application.service;

import com.reeco.common.model.dto.*;
import com.reeco.common.utils.AES;
import com.reeco.ingestion.application.mapper.AlarmMapper;
import com.reeco.ingestion.application.mapper.ConnectionMapper;
import com.reeco.ingestion.application.mapper.ParameterMapper;
import com.reeco.ingestion.application.usecase.StoreConfigUseCase;
import com.reeco.ingestion.cache.model.AlarmRuleCache;
import com.reeco.ingestion.cache.service.AlarmCacheUseCase;
import com.reeco.ingestion.cache.service.IndicatorCacheUseCase;
import com.reeco.ingestion.cache.service.RuleEngineCacheUseCase;
import com.reeco.ingestion.domain.ParamAndAlarm;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.AlarmInfo;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.ConnectionInfo;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.EventStatisticMetaInfo;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.ParamsByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.AlarmInfoRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.ConnectionInfoRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.EventStatisticInfoRepository;
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
public class StoreConfigService implements StoreConfigUseCase {
    @Autowired
    ParamsByOrgRepository paramsByOrgRepository;

    @Autowired
    AlarmInfoRepository alarmInfoRepository;

    @Autowired
    AlarmMapper alarmMapper;

    @Autowired
    ParameterMapper parameterMapper;

    @Autowired
    EventStatisticInfoRepository eventStatisticInfoRepository;

    @Autowired
    AlarmCacheUseCase alarmCacheUseCase;

    @Autowired
    RuleEngineCacheUseCase ruleEngineCacheUseCase;

    @Autowired
    IndicatorCacheUseCase indicatorCacheUseCase;

    @Autowired
    ConnectionInfoRepository connectionInfoRepository;

    @Autowired
    ConnectionMapper connectionMapper;

    private final LocalDateTime defaultDate = LocalDateTime.now().minusDays(1L);

    @Override
    public void storeParameter(Parameter parameter) {
        ParamsByOrg paramsByOrg = parameterMapper.toPersistence(parameter);

        EventStatisticMetaInfo.Key eventMetaInfoKey = new EventStatisticMetaInfo.Key(parameter.getOrganizationId(),
                parameter.getId());

        EventStatisticMetaInfo eventStatisticMetaInfo = eventStatisticInfoRepository
                .findById(eventMetaInfoKey).block();

        if (eventStatisticMetaInfo == null){
            eventStatisticInfoRepository.save(new EventStatisticMetaInfo(
                    eventMetaInfoKey,
                    LocalDateTime.now(),
                    LocalDateTime.now().minusDays(2L)
            )).subscribe(v->log.info("Update Event Statistic Meta Info {}", v));
        }
//      delete old alarm
        List<AlarmInfo> alarmInfOLdo = alarmInfoRepository
                .findByOrgAndParam(parameter.getOrganizationId(), parameter.getId())
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Create param", parameter.toString());
                    return Mono.empty();
                }))
                .collectList().block();

        if (alarmInfOLdo != null) {
            if (alarmInfOLdo.size() > 0) {
                alarmInfoRepository.deleteAll(alarmInfOLdo).subscribe();
                log.info("Deleted old alarms: {}", alarmInfOLdo.toString());
                List<String> evictKeys = alarmInfOLdo.stream().map(v -> v.getPartitionKey().getAlarmId().toString()).collect(Collectors.toList());
                ruleEngineCacheUseCase.evict(evictKeys);
            }
            alarmCacheUseCase.evict(parameter.getId().toString());
            log.info("Evict Old Alarms From Cache: [{}]", parameter.getId().toString());
        }
        List<Alarm> alarms = parameter
                .getAlarms()
                .stream()
                .peek(v -> {
                    v.setOrganizationId(parameter.getOrganizationId());
                    v.setParamId(parameter.getId());
                    v.setIndicatorId(parameter.getIndicatorId());
                }).collect(Collectors.toList());

        List<AlarmInfo> alarmInfoList = alarms
                .stream()
                .map(v -> {
                    AlarmInfo alarmInfo = alarmMapper.toPersistence(v);
                    alarmInfo.setUpdatedAt(LocalDateTime.now());
                    return alarmInfo;
                }).collect(Collectors.toList());

        List<AlarmRuleCache> alarmRuleCaches = alarms
                .stream()
                .map(v -> new AlarmRuleCache(v.getId(), LocalDateTime.now(), 0L, false))
                .collect(Collectors.toList());

        alarmInfoRepository.saveAll(alarmInfoList).subscribe(v -> log.info("Saved alarms: {}", v));
        alarmCacheUseCase.put(new ParamAndAlarm(parameter.getId(), alarms));

        ruleEngineCacheUseCase.put(alarmRuleCaches);
        paramsByOrgRepository.save(paramsByOrg).subscribe(v -> log.info("Saved param: {}", v));
    }

    @Override
    public void deleteParameter(Parameter parameter) {
        ParamsByOrg.Key paramsByOrgKey = new ParamsByOrg.Key(parameter.getOrganizationId(), parameter.getId());
        EventStatisticMetaInfo.Key eventStatMetaKey = new EventStatisticMetaInfo.Key(parameter.getOrganizationId(), parameter.getId());

        List<AlarmInfo> alarmInfo = alarmInfoRepository
                .findByOrgAndParam(parameter.getOrganizationId(), parameter.getId())
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Alarm not found with: {}", parameter.toString());
                    return Mono.empty();
                }))
                .collectList().block();

        if (alarmInfo != null) {
            if (alarmInfo.size() > 0) {
                alarmInfoRepository.deleteAll(alarmInfo).subscribe();
                log.info("Deleted alarms: {}", alarmInfo.toString());
                List<String> evictKeys = alarmInfo.stream().map(v -> v.getPartitionKey().getAlarmId().toString()).collect(Collectors.toList());
                ruleEngineCacheUseCase.evict(evictKeys);
            }
            alarmCacheUseCase.evict(parameter.getId().toString());
            log.info("Evict Alarms From Cache: [{}]", parameter.getId().toString());
        }
        paramsByOrgRepository.deleteById(paramsByOrgKey).subscribe();
        log.info("Deleted param: {}", paramsByOrgKey.toString());
        eventStatisticInfoRepository.deleteById(eventStatMetaKey).subscribe();
        log.info("Deleted event statistic meta: {}", eventStatMetaKey.toString());
    }

    @Override
    public void storeConnection(Connection connection){
        if (connection instanceof HTTPConnection){
            storeConnection((HTTPConnection) connection);
        }
        else if (connection instanceof FTPConnection){
            storeConnection((FTPConnection) connection);
        }
        else if (connection instanceof DataSharingConnection){
            storeConnection((DataSharingConnection) connection);
        }
    }

    public void storeConnection(HTTPConnection httpConnection){
        httpConnection.setAccessToken(AES.decrypt(httpConnection.getAccessToken()));
        ConnectionInfo connectionInfo = connectionMapper.toPersistence(httpConnection);
        connectionInfoRepository.save(connectionInfo).subscribe(v -> log.info("Saved connection: {}", v));
    }

    @Override
    public void deleteConnection(Connection connection){
        ConnectionInfo.Key connectionKey =  new ConnectionInfo.Key(connection.getOrganizationId(), connection.getId());
        connectionInfoRepository.deleteById(connectionKey).subscribe();
        log.info("Deleted connection: {}", connectionKey.toString());
    }

    public void storeConnection(FTPConnection ftpConnection){
        ConnectionInfo.Key key = new ConnectionInfo.Key(ftpConnection.getOrganizationId(), ftpConnection.getId());
        ConnectionInfo  connectionInfo = new ConnectionInfo(key, ftpConnection.getProtocol(), null,ftpConnection.getEnglishName(),
                ftpConnection.getVietnameseName(),null, ftpConnection.getWorkspaceId(), ftpConnection.getReceivedAt(),null,null);
        connectionInfoRepository.save(connectionInfo).subscribe(v -> log.info("Saved connection: {}", v));
    }

    public void storeConnection(DataSharingConnection dataSharingConnection){
        ConnectionInfo connectionInfo = connectionMapper.toPersistence(dataSharingConnection);
        connectionInfoRepository.save(connectionInfo).subscribe(v -> log.info("Saved connection: {}", v));
    }
}
