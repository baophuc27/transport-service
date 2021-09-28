package com.reeco.core.dmp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories({"com.marvin.core.dmp.persistence.mongo"})
public class MongoDBConfiguration {
}

