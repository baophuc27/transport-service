package com.reeco.ingestion.infrastructure;

import com.reeco.ingestion.infrastructure.queue.ByteSerializer;
import com.reeco.ingestion.infrastructure.queue.KafkaBaseMsg;
import com.reeco.ingestion.infrastructure.queue.KafkaMsgCallback;
import com.reeco.ingestion.infrastructure.model.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

@RequiredArgsConstructor
@Slf4j
public class KafkaMessageProducer {
    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @Autowired
    private ByteSerializer byteSerializer;

    @Value(value = "${spring.kafka.topic.data.name}")
    private String sendDataTopicName;

    @Value(value = "${spring.kafka.topic.response.name}")
    private String sendResponseTopicName;

    public void sendDataRecord(DataRecordMessage message){
        byte[] data = byteSerializer.getBytes(message);
        String key = message.getStation_id().toString();
        KafkaBaseMsg kafkaMsg = new KafkaBaseMsg(key,data,null);
        send(sendDataTopicName,kafkaMsg,kafkaTemplate);
    }

    public void sendStatusResponse(ResponseMessage message){
        byte[] data = byteSerializer.getBytes(message);
        String key = null;
        KafkaBaseMsg kafkaBaseMsg = new KafkaBaseMsg(key,data,null);
        send(sendDataTopicName,kafkaBaseMsg,kafkaTemplate);
    }

    private void send(String topicName, KafkaBaseMsg message, KafkaTemplate<String,byte[]> template){
        ListenableFuture<SendResult<String,byte[]>> future = template.send(topicName, message.getKey(),message.getValue());
        future.addCallback(new KafkaMsgCallback());
    }
}
