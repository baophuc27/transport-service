package com.reeco.ingestion.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.reeco.ingestion.adapter.out.TsEventPersistenceAdapter;
import com.reeco.ingestion.application.port.out.AggregateEventsPort;
import com.reeco.ingestion.application.port.out.IndicatorRepository;
import com.reeco.ingestion.adapter.out.EventStatPersistenceAdapter;
import com.reeco.ingestion.adapter.out.IndicatorPersistenceAdapter;
import com.reeco.ingestion.application.port.out.InsertEventPort;
import com.reeco.ingestion.application.port.out.NumStatRepository;
import com.reeco.ingestion.application.service.*;
import com.reeco.ingestion.application.usecase.*;
import com.reeco.ingestion.cache.service.*;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IngestionServiceConfiguration {

    /* *
    * Use case Configuration
    * */
    @Bean
    EntityManagementUseCase entityManagementUseCase(IndicatorRepository indicatorRepository){
        return new EntityManagementService(indicatorRepository);
    }

    @Bean
    StoreTsEventUseCase storeTsEventUseCase(){
        return new IncomingTsEventService();
    }

    @Bean
    public CacheManager cacheManager() {
        String[] cacheNames = {
                "alarm_cache", "indicator_cache", "alarm_rule_cache"
        };
        return new ConcurrentMapCacheManager(cacheNames);
    }

    @Bean
    StoreConfigUseCase storeConfigUseCase(CacheManager cacheManager){
        return new StoreConfigService();
    }

    @Bean
    RuleEngineCacheUseCase ruleEngineCacheUseCase(CacheManager cacheManager){
        return new RuleEngineCacheService();
    }

    @Bean
    RuleEngineUseCase ruleEngineUseCase(){
        return new RuleEngineService();
    }
    @Bean
    AlarmCacheUseCase alarmCacheUseCase(CacheManager cacheManager){
        return new AlarmCacheService();
    }

    @Bean
    StatisticEventUseCase statisticEventUseCase() {
        return new StatisticEventService();
    }

    @Bean
    IndicatorCacheUseCase indicatorCacheUseCase() {
        return new IndicatorCacheService();
    }

    @Bean
    InsertEventPort tsEventRepository(){
        return new TsEventPersistenceAdapter();
    }

    @Bean
    IndicatorRepository indicatorRepository() {
        return new IndicatorPersistenceAdapter();
    }


    @Bean
    AggregateEventsPort aggregateEventsPort() {return new EventStatPersistenceAdapter();
    }
}
