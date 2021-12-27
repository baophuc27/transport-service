package com.reeco.ingestion.configuration;

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

    @Value(value = "${kafka.topics.event-topic.name}")
    private String eventTopic;

    @Value(value = "${kafka.topics.event-topic.partitions}")
    private Integer eventTopicPartitions;

    @Value(value = "${kafka.topics.event-topic.replications}")
    private Integer eventTopicReplications;

    @Value(value = "${kafka.topics.config-topic.properties}")
    private String eventProps;

    @Value(value = "${kafka.topics.config-topic.name}")
    private String configTopic;

    @Value(value = "${kafka.topics.config-topic.partitions}")
    private Integer configTopicPartitions;

    @Value(value = "${kafka.topics.config-topic.replications}")
    private Integer configTopicReplications;

    @Value(value = "${kafka.topics.config-topic.properties}")
    private String configProps;

    @Value(value = "${kafka.topics.alarm-topic.name}")
    private String alarmTopic;

    @Value(value = "${kafka.topics.alarm-topic.partitions}")
    private Integer alarmTopicPartitions;

    @Value(value = "${kafka.topics.alarm-topic.replications}")
    private Integer alarmTopicReplications;

    @Value(value = "${kafka.topics.alarm-topic.properties}")
    private String alarmProps;

    @Value(value = "${kafka.topics.alarm-noti-topic.name}")
    private String alarmNotiTopic;

    @Value(value = "${kafka.topics.alarm-noti-topic.partitions}")
    private Integer alarmNotiTopicPartitions;

    @Value(value = "${kafka.topics.alarm-noti-topic.replications}")
    private Integer alarmNotiTopicReplications;

    @Value(value = "${kafka.topics.alarm-noti-topic.properties}")
    private String alarmNotiProps;


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
    public NewTopic createEventTopic() {
        Map<String, String> eventConfigs = getConfigs(eventProps);
        NewTopic newTopic = new NewTopic(eventTopic, eventTopicPartitions, eventTopicReplications.shortValue());
        newTopic.configs(eventConfigs);
        return newTopic;
    }

    @Bean
    public NewTopic createConfigTopic() {
        Map<String, String> configs = getConfigs(configProps);
        NewTopic newTopic = new NewTopic(configTopic, configTopicPartitions, configTopicReplications.shortValue());
        newTopic.configs(configs);
        return newTopic;
    }

    @Bean
    public NewTopic createAlarmTopic() {
        Map<String, String> configs = getConfigs(alarmProps);
        NewTopic newTopic = new NewTopic(alarmTopic, alarmTopicPartitions, alarmTopicReplications.shortValue());
        newTopic.configs(configs);
        return newTopic;
    }

    @Bean
    public NewTopic createAlarmNotiTopic() {
        Map<String, String> configs = getConfigs(alarmNotiProps);
        NewTopic newTopic = new NewTopic(alarmNotiTopic, alarmNotiTopicPartitions, alarmNotiTopicReplications.shortValue());
        newTopic.configs(configs);
        return newTopic;
    }

}