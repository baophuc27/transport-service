package com.reeco.ingestion.infrastructure.queue.kafka;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Log4j2
@ConfigurationProperties(prefix = "kafka.settings")
@Component
@Getter
@Setter
public class KafkaProducerConfig implements KafkaProducerFactory{

    private String bootstrapServers;
    private int retries;
    private int batchSize;
    private int lingerMs;
    private int bufferMemory;
    private String acks;
    private String keySerializer;
    private String valueSerializer;
    private List<KeyValueProperties> other;
    private short replicationFactor;

    @Getter
    public static class KeyValueProperties {
        private String key;
        private String value;
    }

    public Map<String, Object> toProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        props.put(ProducerConfig.ACKS_CONFIG, acks);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        if (other != null) {
            other.forEach(kv -> props.put(kv.getKey(), kv.getValue()));
        }
        return props;
    }

    @Bean
    public ProducerFactory<String, byte[]> RequestMsgFactory() {
        Map<String, Object> configProps = toProps();
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        log.info("CONFIG : " + configProps);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, byte[]> producerTemplate() {
        return new KafkaTemplate<>(RequestMsgFactory());
    }

}