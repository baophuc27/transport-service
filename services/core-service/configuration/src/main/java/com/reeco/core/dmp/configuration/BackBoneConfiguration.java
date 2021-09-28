package com.reeco.core.dmp.configuration;

import com.reeco.core.dmp.application.port.in.ViewQuery;
import com.reeco.core.dmp.application.port.out.ViewRepository;
import com.reeco.core.dmp.application.service.ViewQueryService;
import com.reeco.core.dmp.persistence.mongo.ViewPersistence;
import com.reeco.core.dmp.persistence.mongo.ReactiveMongoViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BackBoneConfiguration {

    private final ReactiveMongoViewRepository reactiveMongoViewRepository;

    @Autowired
    BackBoneConfiguration(final ReactiveMongoViewRepository reactiveMongoViewRepository){
        this.reactiveMongoViewRepository = reactiveMongoViewRepository;
    }

    @Bean
    ViewRepository accountRepository() {
        return new ViewPersistence(reactiveMongoViewRepository);
    }

    @Bean
    ViewQuery accountQuery(final ViewRepository viewRepository){
        return  new ViewQueryService(accountRepository());
    }
}
