package com.reeco.ingestion.infrastructure.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.kafka.clients.producer.RecordMetadata;

@Data
@AllArgsConstructor
public class KafkaMsgMetadata {
    private RecordMetadata metadata;
}
