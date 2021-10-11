package com.reeco.ingestion.infrastructure.queue.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KafkaProducerTemplate implements QueueProducer {

    @Autowired
    private KafkaTemplate<String, byte[]> producerTemplate;

    @Override
    public void send(KafkaTopicInfo tpi, QueueMsg msg,  ListenableFutureCallback<SendResult<String, byte[]>> callback) {
        ProducerRecord<String, byte[]> record = getRecord(msg, tpi);
        ListenableFuture<SendResult<String, byte[]>> future = producerTemplate.send(record);
        future.addCallback(callback);
    }

    @Override
    public SendResult<String, byte[]> send(KafkaTopicInfo tpi, QueueMsg msg) throws ExecutionException, InterruptedException {
        ProducerRecord<String, byte[]> record = getRecord(msg, tpi);
        SendResult<String, byte[]> future = producerTemplate.send(record).get();
        return future;
    }

    private ProducerRecord<String, byte[]> getRecord(QueueMsg msg, KafkaTopicInfo tpi){
        String key = msg.getKey();
        byte[] data = msg.getData();
        Iterable<Header> headers = msg.getHeaders().getData().entrySet().stream().map(e -> new RecordHeader(e.getKey(), e.getValue())).collect(Collectors.toList());
        return new ProducerRecord<>(tpi.getFullTopicName(), tpi.getPartition(), null, key, data, headers);
    }
}
