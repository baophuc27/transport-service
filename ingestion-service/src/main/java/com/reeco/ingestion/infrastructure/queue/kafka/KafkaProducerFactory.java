package com.reeco.ingestion.infrastructure.queue.kafka;

import org.springframework.kafka.core.ProducerFactory;

public interface KafkaProducerFactory {

    ProducerFactory<String, byte[]> RequestMsgFactory();

}