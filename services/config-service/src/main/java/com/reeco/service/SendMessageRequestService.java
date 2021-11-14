package com.reeco.service;

import com.reeco.event.RequestMsg;
import com.reeco.framework.SendMessageRequest;
import com.reeco.framework.UseCase;
import com.reeco.kafka.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;

@UseCase
public class SendMessageRequestService implements SendMessageRequest {

    @Autowired
    private QueueProducer producer;

    @Autowired
    private KafkaTopicConfig kafkaTopicConfig;

    @Override
    public void sendMessage(RequestMsg msg, ListenableFutureCallback<SendResult<String, byte[]>> callBack) {
        KafkaTopicInfo tpi = getKafkaTopicInfo(msg);
        producer.send(tpi, new KafkaJsonRequestMsg<>(msg), callBack);

    }

    @Override
    public SendResult<String, byte[]> sendMessage(RequestMsg msg) throws ExecutionException, InterruptedException {
        KafkaTopicInfo tpi = getKafkaTopicInfo(msg);
        SendResult<String, byte[]> future = producer.send(tpi, new KafkaJsonRequestMsg<>(msg));
        return future;
    }

    private KafkaTopicInfo getKafkaTopicInfo(RequestMsg msg){
        Integer partition = msg.getOrgId().intValue() % kafkaTopicConfig.getNumPartitions();
        KafkaTopicInfo tpi = new KafkaTopicInfo(kafkaTopicConfig.getTopicName(), partition);
        return tpi;
    }
}