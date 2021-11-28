package com.reeco.ingestion.cache.config;


import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.NearCacheConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@EnableCaching
@Profile("client")
public class ClientCacheConfig {
    @Bean
    ClientConfig config() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.addNearCacheConfig(alarmNearCacheConfig());
        clientConfig.addNearCacheConfig(indicatorNearCacheConfig());
        clientConfig.addNearCacheConfig(alarmRuleNearCacheConfig());
        return clientConfig;
    }

    private NearCacheConfig alarmNearCacheConfig() {
        NearCacheConfig nearCacheConfig = new NearCacheConfig();
        nearCacheConfig.setName("alarm_info_cache");
        nearCacheConfig.setTimeToLiveSeconds(300);
        return nearCacheConfig;
    }

    private NearCacheConfig indicatorNearCacheConfig() {
        NearCacheConfig nearCacheConfig = new NearCacheConfig();
        nearCacheConfig.setName("indicator_cache");
        nearCacheConfig.setTimeToLiveSeconds(300);
        return nearCacheConfig;
    }

    private NearCacheConfig alarmRuleNearCacheConfig() {
        NearCacheConfig nearCacheConfig = new NearCacheConfig();
        nearCacheConfig.setName("alarm_rule_cache");
        return nearCacheConfig;
    }

}
