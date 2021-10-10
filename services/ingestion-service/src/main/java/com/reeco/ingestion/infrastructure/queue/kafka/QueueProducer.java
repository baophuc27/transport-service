package com.reeco.ingestion.infrastructure.queue.kafka;

import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;

public interface QueueProducer{

    void send(KafkaTopicInfo tpi, QueueMsg msg, ListenableFutureCallback<SendResult<String, byte[]>> callback);

    SendResult<String, byte[]> send(KafkaTopicInfo tpi, QueueMsg msg) throws ExecutionException, InterruptedException;
}