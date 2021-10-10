package com.reeco.ingestion.infrastructure.queue.kafka;

import lombok.Getter;
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
public class KafkaTopicConfig {

    @Value(value = "${kafka.settings.bootstrapServers}")
    private String bootstrapServers;

    @Value("${kafka.topic.core.properties}")
    private String coreProperties;

    @Value(value = "${kafka.topic.prefix}")
    private String topicPrefix;

    @Value(value = "${kafka.topic.core.name}")
    private String topicName;

    @Value(value = "${kafka.topic.core.partitions}")
    private Integer numPartitions;

    private Map<String, String> coreConfigs;

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
    public NewTopic createCoreTopic() {
        coreConfigs = getConfigs(coreProperties);
        String fullTopicName = topicPrefix + "_" + topicName;
        NewTopic newTopic = new NewTopic(fullTopicName, numPartitions, (short) 1);
        newTopic.configs(coreConfigs);
        return newTopic;
    }
}