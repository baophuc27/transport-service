package com.reeco.config.kafka;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Getter
@Log4j2
public class KafkaTopicsConfiguration {

    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value(value = "${kafka.topics.config-topic.name}")
    private String configTopic;

    @Value(value = "${kafka.topics.config-topic.properties}")
    private String configProps;


    private Map<String, String> getConfigs(String properties) {
        Map<String, String> configs = new HashMap<>();
        for (String property : properties.split(";")) {
            int delimiterPosition = property.indexOf(":");
            String key = property.substring(0, delimiterPosition);
            String value = property.substring(delimiterPosition + 1);
            configs.put(key, value);
        }
        return configs;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }


    @Bean
    public NewTopic createConfigTopic() {
        Map<String, String> configs = getConfigs(configProps);
        NewTopic newTopic = new NewTopic(configTopic,2,(short) 1);
        newTopic.configs(configs);
        return newTopic;
    }

}