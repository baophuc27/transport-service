package com.reeco.ingestion.infrastructure.queue.kafka;

import java.util.Map;

public interface MessageHeaders {

    byte[] put(String key, byte[] value);

    byte[] get(String key);

    Map<String, byte[]> getData();
}