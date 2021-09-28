package com.reeco.ingestion.configuration;

import com.reeco.ingestion.adapter.in.DeleteDeviceAdapter;
import com.reeco.ingestion.adapter.in.ReceiveFileAdapter;
import com.reeco.ingestion.adapter.out.*;
import com.reeco.ingestion.application.port.out.*;
import com.reeco.ingestion.infrastructure.*;
import com.reeco.transport.adapter.out.*;
import com.reeco.ingestion.application.port.in.DeleteDevicePort;
import com.reeco.ingestion.application.port.in.ReceiveFilePort;
import com.reeco.transport.application.port.out.*;
import com.reeco.ingestion.application.repository.FtpRepository;
import com.reeco.ingestion.application.usecase.DeviceManagementUseCase;
import com.reeco.ingestion.application.port.in.RegisterDevicePort;
import com.reeco.ingestion.application.usecase.DataManagementUseCase;
import com.reeco.ingestion.application.service.DeviceManagementService;
import com.reeco.ingestion.application.service.DataManagementService;
import com.reeco.ingestion.domain.ServiceConnection;
import com.reeco.transport.infrastructure.*;
import com.reeco.ingestion.infrastructure.persistence.stimulate.InMemFtpRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransportConfiguration {

    @Bean
    ServiceConnection serviceConnection(){
        return ServiceConnection.getInstance();
    }

    /* *
    * INFRASTRUCTURE CONFIGURATION
     * */
    @Bean
    MainStorage mainStorage(){
        return new HdfsStorage();
    }

    @Bean
    KafkaMessageSubscriber kafkaMessageProcessor(DeviceManagementUseCase deviceManagementUseCase){
        return new KafkaMessageSubscriber(deviceManagementUseCase);
    }

    /* *
    * Use case Configuration
    * */
    @Bean
    DeviceManagementUseCase deviceManagementUseCase(RegisterDevicePort registerDevicePort,
                                                    BeginReceivingDataPort beginReceivingDataPort,
                                                    StoreConfigurationPort storeConfigurationPort,
                                                    DeleteDevicePort deleteDevicePort,
                                                    SaveAttributePort saveAttributePort){
        return new DeviceManagementService(registerDevicePort,beginReceivingDataPort,storeConfigurationPort,deleteDevicePort,saveAttributePort);
    }

    @Bean
    DataManagementUseCase dataManagementUseCase(BatchingFilePort batchingFilePort, StreamingDataPort streamingDataPort, ReceiveFilePort receiveFilePort){
        return new DataManagementService(batchingFilePort, streamingDataPort,receiveFilePort);
    }
    /* *
     * PORTS & adapter CONFIGURATION
     * */
    @Bean
    ReceiveFilePort receiveFilePort(){
        return new ReceiveFileAdapter();
    }

    @Bean
    BatchingFilePort batchingFilePort(MainStorage mainStorage){
        return new BatchingFileAdapter(mainStorage);
    }

    @Bean
    BeginReceivingDataPort beginReceivingDataPort(FileProcessor fileProcessor){
        return new SFTPObserveFileAdapter(fileProcessor);
    }

    @Bean
    StoreConfigurationPort storeConfigurationPort(){
        return new PersistenceAdapter();
    }

    @Bean
    StreamingDataPort streamingDataPort(KafkaMessageProducer kafkaMessageProducer){
        return new DataToKafkaMessageAdapter(kafkaMessageProducer);
    }

    @Bean
    DeleteDevicePort deleteDevicePort(){
        return new DeleteDeviceAdapter();
    }

    @Bean
    KafkaMessageProducer kafkaMessageProducer( ){
        return new KafkaMessageProducer();
    }

    @Bean
    FtpRepository ftpRepository(){
        return new InMemFtpRepository();
    }

    @Bean
    SaveAttributePort saveAttributePort(){
        return new AttributeAdapter();
    }
}
