package com.reeco.transport.infrastructure.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.topic.data.name}")
    private String sendDataTopicName;

    @Value(value = "${spring.kafka.topic.data.partition}")
    private int sendDataTopicPartition;

    @Value(value = "${spring.kafka.topic.data.replication-factor}")
    private short sendDataTopicReplicationFactor;


    @Bean
    public NewTopic sendDataTopic(){
        return TopicBuilder.name(sendDataTopicName)
                .partitions(sendDataTopicPartition)
                .replicas(sendDataTopicReplicationFactor)
                .build();
    }

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
}
