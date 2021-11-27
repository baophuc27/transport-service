package com.reeco.ingestion.infrastructure.queue.kafka;

public interface QueueMsg {

    String getKey();

    MessageHeaders getHeaders();

    byte[] getData();
}
