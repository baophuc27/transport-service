package com.reeco.http.configuration;

import com.datastax.oss.driver.api.core.session.Session;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.CassandraConverter;

@Configuration
public class TransportHttpConfiguration {
    @Bean
    public CacheManager cacheManager() {
        String[] cacheNames = {
                "param_cache", "connection_cache"
        };
        return new ConcurrentMapCacheManager(cacheNames);
    }


}
