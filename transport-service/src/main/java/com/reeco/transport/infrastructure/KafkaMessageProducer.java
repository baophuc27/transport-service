package com.reeco.transport.infrastructure;

import com.reeco.transport.infrastructure.kafka.ByteSerializer;
import com.reeco.transport.infrastructure.kafka.KafkaBaseMsg;
import com.reeco.transport.infrastructure.kafka.KafkaMsgCallback;
import com.reeco.common.model.dto.AlarmMessage;
import com.reeco.transport.infrastructure.model.DataRecordMessage;
import com.reeco.transport.infrastructure.model.ResponseMessage;
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

    @Value(value = "reeco_time_series_event")
    private String sendDataTopicName;

    @Value(value = "${spring.kafka.topic.response.name}")
    private String sendResponseTopicName;

    @Value(value = "reeco_connection_noti_event")
    private String sendAlarmTopicName;


    public void sendDataRecord(DataRecordMessage message){
        byte[] data = byteSerializer.getBytes(message);
        String key = message.getStationId().toString();
        KafkaBaseMsg kafkaMsg = new KafkaBaseMsg(key,data,null);
        send(sendDataTopicName,kafkaMsg,kafkaTemplate);
    }

    public void sendStatusResponse(ResponseMessage message){
        byte[] data = byteSerializer.getBytes(message);
        String key = null;
        KafkaBaseMsg kafkaBaseMsg = new KafkaBaseMsg(key,data,null);
        send(sendDataTopicName,kafkaBaseMsg,kafkaTemplate);
    }

    public void sendAlarmMessage(AlarmMessage message){
        byte[] data = byteSerializer.getBytes(message);
        String key = null;
        KafkaBaseMsg kafkaBaseMsg = new KafkaBaseMsg(key,data,null);
        send(sendAlarmTopicName,kafkaBaseMsg,kafkaTemplate);
    }

    private void send(String topicName, KafkaBaseMsg message, KafkaTemplate<String,byte[]> template){
        ListenableFuture<SendResult<String,byte[]>> future = template.send(topicName, message.getKey(),message.getValue());
        future.addCallback(new KafkaMsgCallback());
    }
}
