package com.reeco.ingestion.configuration;

import com.reeco.ingestion.adapter.in.IncomingTsEventController;
import com.reeco.ingestion.infrastructure.*;
import com.reeco.transport.adapter.out.*;
import com.reeco.transport.application.port.out.*;
import com.reeco.ingestion.application.port.out.TsEventRepository;
import com.reeco.ingestion.application.usecase.EntityManagementUseCase;
import com.reeco.ingestion.application.usecase.StoreTsEventUseCase;
import com.reeco.ingestion.application.service.EntityManagementService;
import com.reeco.ingestion.application.service.DataManagementService;
import com.reeco.transport.infrastructure.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IngestionServiceConfiguration {

    @Bean
    ServiceConnection serviceConnection(){
        return ServiceConnection.getInstance();
    }

    /* *
    * INFRASTRUCTURE CONFIGURATION
     * */
//    @Bean
//    MainStorage mainStorage(){
//        return new HdfsStorage();
//    }

    @Bean
    KafkaMessageSubscriber kafkaMessageProcessor(EntityManagementUseCase deviceManagementUseCase){
        return new KafkaMessageSubscriber(deviceManagementUseCase);
    }

    /* *
    * Use case Configuration
    * */
    @Bean
    EntityManagementUseCase deviceManagementUseCase(RegisterDevicePort registerDevicePort,
                                                    BeginReceivingDataPort beginReceivingDataPort,
                                                    StoreConfigurationPort storeConfigurationPort,
                                                    DeleteDevicePort deleteDevicePort,
                                                    SaveAttributePort saveAttributePort){
        return new EntityManagementService(registerDevicePort,beginReceivingDataPort,storeConfigurationPort,deleteDevicePort,saveAttributePort);
    }

    @Bean
    StoreTsEventUseCase dataManagementUseCase(BatchingFilePort batchingFilePort, StreamingDataPort streamingDataPort, ReceiveFilePort receiveFilePort){
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
        return new IncomingTsEventController();
    }

    @Bean
    KafkaMessageProducer kafkaMessageProducer( ){
        return new KafkaMessageProducer();
    }

    @Bean
    TsEventRepository ftpRepository(){
        return new InMemFtpRepository();
    }

    @Bean
    SaveAttributePort saveAttributePort(){
        return new AttributeAdapter();
    }
}
