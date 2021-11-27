package com.reeco.ingestion.configuration;

import com.reeco.ingestion.adapter.out.EventStatPersistenceAdapter;
import com.reeco.ingestion.adapter.out.IndicatorPersistenceAdapter;
import com.reeco.ingestion.adapter.out.TsEventPersistenceAdapter;
import com.reeco.ingestion.application.port.out.AggregateEventsPort;
import com.reeco.ingestion.application.port.out.IndicatorRepository;
import com.reeco.ingestion.application.port.out.InsertEventPort;
import com.reeco.ingestion.application.service.EntityManagementService;
import com.reeco.ingestion.application.service.IncomingTsEventService;
import com.reeco.ingestion.application.service.StoreConfigService;
import com.reeco.ingestion.application.usecase.EntityManagementUseCase;
import com.reeco.ingestion.application.usecase.StoreConfigUseCase;
import com.reeco.ingestion.application.usecase.StoreTsEventUseCase;
import com.reeco.ingestion.cache.service.AlarmCacheService;
import com.reeco.ingestion.cache.service.AlarmCacheUseCase;
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
    EntityManagementUseCase entityManagementUseCase(IndicatorRepository indicatorRepository) {
        return new EntityManagementService(indicatorRepository);
    }

    @Bean
    StoreTsEventUseCase storeTsEventUseCase(InsertEventPort tsEventRepository, IndicatorRepository indicatorRepository) {
        return new IncomingTsEventService(tsEventRepository, indicatorRepository);
    }

    @Bean
    public CacheManager cacheManager() {
        System.out.println("Creating cache manager.");
        return new ConcurrentMapCacheManager("alarm_cache");
    }

    @Bean
    StoreConfigUseCase storeConfigUseCase(CacheManager cacheManager) {
        return new StoreConfigService(cacheManager);
    }

    @Bean
    AlarmCacheUseCase alarmCacheUseCase(CacheManager cacheManager) {
        return new AlarmCacheService(cacheManager);
    }


//    @Bean
//    UpdateStatEventUseCase updateStatEventUseCase(AggregateEventsPort aggregateEventsPort){
//        return new StatisticEventService(aggregateEventsPort);
//    }

    @Bean
    InsertEventPort tsEventRepository() {
        return new TsEventPersistenceAdapter();
    }

    @Bean
    IndicatorRepository indicatorRepository() {
        return new IndicatorPersistenceAdapter();
    }


    @Bean
    AggregateEventsPort aggregateEventsPort() {
        return new EventStatPersistenceAdapter();
    }

}
