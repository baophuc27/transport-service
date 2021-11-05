package com.reeco.ingestion.configuration;

import com.reeco.ingestion.adapter.out.EventStatPersistenceAdapter;
import com.reeco.ingestion.adapter.out.IndicatorPersistenceAdapter;
import com.reeco.ingestion.adapter.out.TsEventPersistenceAdapter;
import com.reeco.ingestion.application.port.out.*;
import com.reeco.ingestion.application.service.EntityManagementService;
import com.reeco.ingestion.application.service.IncomingTsEventService;
import com.reeco.ingestion.application.service.StatisticEventService;
import com.reeco.ingestion.application.usecase.EntityManagementUseCase;
import com.reeco.ingestion.application.usecase.StoreTsEventUseCase;
import com.reeco.ingestion.application.usecase.UpdateStatEventUseCase;
import com.reeco.ingestion.domain.Parameter;
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
    StoreTsEventUseCase storeTsEventUseCase(InsertEventPort tsEventRepository, IndicatorRepository indicatorRepository){
        return new IncomingTsEventService(tsEventRepository, indicatorRepository);
    }

//    @Bean
//    UpdateStatEventUseCase updateStatEventUseCase(AggregateEventsPort aggregateEventsPort){
//        return new StatisticEventService(aggregateEventsPort);
//    }

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
