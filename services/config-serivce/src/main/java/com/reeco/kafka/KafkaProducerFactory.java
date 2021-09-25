package com.reeco.kafka;

import org.springframework.kafka.core.ProducerFactory;

public interface KafkaProducerFactory {

    ProducerFactory<String, byte[]> RequestMsgFactory();

}