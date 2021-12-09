package com.reeco.http.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
