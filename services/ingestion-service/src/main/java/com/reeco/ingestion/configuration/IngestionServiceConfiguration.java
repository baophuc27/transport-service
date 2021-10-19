package com.reeco.ingestion.configuration;

import com.reeco.ingestion.adapter.out.IndicatorPersistenceAdapter;
import com.reeco.ingestion.adapter.out.TsEventPersistenceAdapter;
import com.reeco.ingestion.application.mapper.NumericalTsEventMapper;
import com.reeco.ingestion.application.port.out.IndicatorRepository;
import com.reeco.ingestion.application.port.out.TsEventRepository;
import com.reeco.ingestion.application.service.EntityManagementService;
import com.reeco.ingestion.application.service.IncomingTsEventService;
import com.reeco.ingestion.application.usecase.EntityManagementUseCase;
import com.reeco.ingestion.application.usecase.StoreTsEventUseCase;
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
    StoreTsEventUseCase storeTsEventUseCase(TsEventRepository tsEventRepository, IndicatorRepository indicatorRepository){
        return new IncomingTsEventService(tsEventRepository, indicatorRepository);
    }

    @Bean
    TsEventRepository tsEventRepository(){
        return new TsEventPersistenceAdapter();
    }


    @Bean
    IndicatorRepository indicatorRepository() {
        return new IndicatorPersistenceAdapter();
    }

}
