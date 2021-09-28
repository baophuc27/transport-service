package com.reeco.kafka;

import org.apache.kafka.clients.producer.RecordMetadata;

public interface MsgCallBack {
    void onSuccess(RecordMetadata metadata);

    void onFailure(Throwable cause);

}
