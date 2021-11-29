package com.reeco.ingestion.cache.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@EnableCaching
@Profile("embedded")
public class EmbeddedCacheConfig {
    @Bean
    Config config() {
        Config config = new Config();

        MapConfig alarmConfig = new MapConfig();
        MapConfig indicatorConfig = new MapConfig();
        MapConfig alarmRuleConfig = new MapConfig();
        alarmConfig.setTimeToLiveSeconds(300);
        config.getMapConfigs().put("alarm_info_cache", alarmConfig);
        config.getMapConfigs().put("indicator_cache", indicatorConfig);
        config.getMapConfigs().put("alarm_rule_cache", alarmRuleConfig);
        return config;
    }

}
