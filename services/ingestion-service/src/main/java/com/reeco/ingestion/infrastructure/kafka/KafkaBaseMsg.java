package com.reeco.ingestion.infrastructure.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class KafkaBaseMsg {
    String key;

    byte[] value;

    KafkaMsgHeader headers;

}
