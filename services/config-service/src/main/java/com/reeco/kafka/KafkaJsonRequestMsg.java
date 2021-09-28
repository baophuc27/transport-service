package com.reeco.kafka;

import com.reeco.event.RequestMsg;
import lombok.Data;

@Data
public class KafkaJsonRequestMsg<T extends RequestMsg> implements QueueMsg {
    private final String key;
    protected final T value;
    private final MessageHeaders headers;

    public KafkaJsonRequestMsg(T value) {
        this.key = value.getStationId().toString();
        this.value = value;
        this.headers = value.buildHeaders();
    }

    public KafkaJsonRequestMsg(String key, T value) {
        this(key, value, new DefaultMsgHeaders());
    }

    public KafkaJsonRequestMsg(String key, T value, MessageHeaders headers) {
        this.key = key;
        this.value = value;
        this.headers = headers;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public MessageHeaders getHeaders() {
        return headers;
    }

    @Override
    public byte[] getData() {
        return value.toByteArray();
    }
}
