package com.reeco.ingestion.infrastructure.queue.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

public interface QueueConsumer {

    void listen(Map<String,byte[]> header, ConsumerRecord<String,byte[]> message);

}