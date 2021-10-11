package com.reeco.ingestion.infrastructure.queue.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface QueueConsumer {

    void listen(Map<String,byte[]> header, ConsumerRecord<String,byte[]> message);

}