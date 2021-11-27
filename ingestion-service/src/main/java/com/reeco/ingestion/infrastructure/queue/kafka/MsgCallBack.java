package com.reeco.ingestion.infrastructure.queue.kafka;

import org.apache.kafka.clients.producer.RecordMetadata;

public interface MsgCallBack {
    void onSuccess(RecordMetadata metadata);

    void onFailure(Throwable cause);

}
