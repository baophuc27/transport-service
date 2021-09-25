package com.reeco.kafka;

public interface QueueMsg {

    String getKey();

    MessageHeaders getHeaders();

    byte[] getData();
}
