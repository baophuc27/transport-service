package com.reeco.ingestion.infrastructure.queue;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaMsgHeader {
    String key;

    byte[] value;
}
