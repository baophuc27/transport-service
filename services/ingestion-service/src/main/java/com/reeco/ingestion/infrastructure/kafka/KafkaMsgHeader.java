package com.reeco.ingestion.infrastructure.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaMsgHeader {
    String key;

    byte[] value;
}
