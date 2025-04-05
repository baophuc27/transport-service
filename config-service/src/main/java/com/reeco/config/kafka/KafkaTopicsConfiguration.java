package com.reeco.config.kafka;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class KafkaTopicsConfiguration {

    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value(value = "${kafka.topics.config-topic.name:reeco_config_event}")
    private String configTopic;

    @Value(value = "${kafka.topics.config-topic.properties:retention.ms:432000000;segment.bytes:26214400;retention.bytes:1048576000}")
    private String configProps;

    @Value(value = "${kafka.topics.config-topic.partitions:1}")
    private int partitions;

    @Value(value = "${kafka.topics.config-topic.replications:1}")
    private int replications;

    private Map<String, String> getConfigs(String properties) {
        Map<String, String> configs = new HashMap<>();
        for (String property : properties.split(";")) {
            int delimiterPosition = property.indexOf(":");
            if (delimiterPosition > 0) {
                String key = property.substring(0, delimiterPosition);
                String value = property.substring(delimiterPosition + 1);
                configs.put(key, value);
            }
        }
        return configs;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        log.info("Configuring Kafka admin with bootstrap servers: {}", bootstrapServers);
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic createConfigTopic() {
        log.info("Creating Kafka topic: {}", configTopic);
        Map<String, String> configs = getConfigs(configProps);
        NewTopic newTopic = new NewTopic(configTopic, partitions, (short) replications);
        newTopic.configs(configs);
        return newTopic;
    }
}