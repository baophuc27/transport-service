package com.reeco.ingestion.application.service;

import com.reeco.ingestion.application.port.in.IncomingConfigEvent;
import com.reeco.ingestion.application.port.out.IndicatorRepository;
import com.reeco.ingestion.application.port.out.InsertEventPort;
import com.reeco.ingestion.application.usecase.StoreConfigUseCase;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.IndicatorInfo;
import com.reeco.ingestion.infrastructure.persistence.cassandra.entity.ParamsByOrg;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.AlarmInfoRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.IndicatorInfoRepository;
import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.ParamsByOrgRepository;
import com.reeco.ingestion.utils.annotators.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

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



    @Override
    public void storeConfig(IncomingConfigEvent config){
        Mono<IndicatorInfo> indicatorInfo = indicatorInfoRepository.findById(new IndicatorInfo.Key(config.getParameter().getIndicatorId()));
        IndicatorInfo indicatorInfo1 = indicatorInfo.block();

        ParamsByOrg.Key paramsByOrgKey = new ParamsByOrg.Key(config.getOrgId(),config.getParameter().getId());
        ParamsByOrg paramsByOrg = new ParamsByOrg(paramsByOrgKey, indicatorInfo1.getPartitionKey().getIndicatorId(),indicatorInfo1.getIndicatorName(),
                config.getParameter().getEnglishName(),100L,config.getConnectionId(),indicatorInfo1.getStandardUnit(), LocalDateTime.now());

        paramsByOrgRepository.save(paramsByOrg);
    }
}
