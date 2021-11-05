package com.reeco.ingestion.configuration;

import com.reeco.ingestion.application.port.in.IncomingTsEvent;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.*;


@Configuration
@Log4j2
public class KafkaConsumerConfiguration {

    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${kafka.consumer.event.group-id}")
    private String eventCg;

    @Value(value = "${kafka.consumer.event.auto-offset-reset}")
    private String eventCgResetOffset;

    @Value(value = "${kafka.consumer.config.group-id}")
    private String configCg;


    @Bean
    public ConsumerFactory<String, IncomingTsEvent> eventConsumerFactory() {
        List<String> bootstrapServers = new ArrayList<>(Collections.singletonList(bootstrapAddress));
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, eventCg);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, eventCgResetOffset);
        log.info("CONSUMERS: " + props.toString());
        return new DefaultKafkaConsumerFactory<>(props,
                new StringDeserializer(),
                new JsonDeserializer<>(IncomingTsEvent.class));
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, IncomingTsEvent> timeSeriesEventListener()
    {
        ConcurrentKafkaListenerContainerFactory<String, IncomingTsEvent> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(eventConsumerFactory());
        factory.setAckDiscarded(true);
        return factory;
    }
}