package com.reeco.transport.infrastructure;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.transport.application.mapper.DataRecordMapper;
import com.reeco.transport.infrastructure.model.DataRecordMessage;
import com.reeco.transport.utils.annotators.Infrastructure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.SSLSocketFactory;
import java.util.Arrays;
import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Infrastructure
public class MQTTMessagePublisher {

    @Autowired
    private final ObjectMapper objectMapper;

    private final MqttClient client;
    public MQTTMessagePublisher(ObjectMapper objectMapper) throws MqttException {
        this.objectMapper = objectMapper;
        client = new MqttClient(
                "tcp://localhost:3000", // serverURI in format: "protocol://name:port"
                "admin-dev", // ClientId
                new MemoryPersistence()); // Persistence
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName("admin");
        mqttConnectOptions.setPassword("admin123".toCharArray());
//        mqttConnectOptions.setSocketFactory(SSLSocketFactory.getDefault()); // using the default socket factory
        client.connect(mqttConnectOptions);
        client.setCallback(new MqttCallback() {

            @Override
            public void connectionLost(Throwable cause) { // Called when the client lost the connection to the broker
                System.out.println(cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                System.out.println(topic + ": " + Arrays.toString(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) { // Called when a outgoing publish is complete
//                System.out.println("complete");
            }
        });
    }

    public void publish(String topic, DataRecordMessage message) throws MqttException, JsonProcessingException {

        log.info("Publish message: {} to: {}",message,topic);
        String data = objectMapper.writeValueAsString(message);
        client.publish(topic,data.getBytes(UTF_8),1,false);



    }
}
