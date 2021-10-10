package com.reeco.ingestion.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reeco.ingestion.application.usecase.EntityManagementUseCase;
import com.reeco.ingestion.application.usecase.RegisterDeviceCommand;
import com.reeco.ingestion.infrastructure.queue.kafka.QueueConsumer;
import com.reeco.ingestion.utils.annotators.Adapter;
import com.reeco.ingestion.domain.Device;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Map;


@Adapter
@RequiredArgsConstructor
@Slf4j
public class EntityManagementController implements QueueConsumer {

    private final EntityManagementUseCase entityManagementUseCase;

    private final ObjectMapper objectMapper;

    @Override
    public Device registerDevice(RegisterDeviceCommand registerDeviceCommand){

            Device deviceConnection = connectionMapper.registerCommandToFTPDeviceConnection(registerDeviceCommand);
            log.info("Received register request with device id: {}",deviceConnection.getDeviceId());
            return deviceConnection;
    }

    @Override
    @KafkaListener(topics = "reeco_time_series_ev",containerFactory = "timeSeriesEventListener")
    public void listen(Map<String, byte[]> header, ConsumerRecord<String, byte[]> message) {

    }
}
