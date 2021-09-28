//package com.reeco.transport.infrastructure.ftp;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ftpserver.FtpServer;
//import org.apache.ftpserver.FtpServerFactory;
//import org.apache.ftpserver.filesystem.nativefs.NativeFileSystemFactory;
//import org.apache.ftpserver.ftplet.FileSystemFactory;
//import org.apache.ftpserver.ftplet.Ftplet;
//import org.apache.ftpserver.ftplet.UserManager;
//import org.apache.ftpserver.listener.Listener;
//import org.apache.ftpserver.listener.ListenerFactory;
//import org.apache.ftpserver.ssl.SslConfigurationFactory;
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Scope;
//import org.springframework.integration.dsl.IntegrationFlow;
//import org.springframework.integration.dsl.IntegrationFlows;
//import org.springframework.integration.dsl.MessageChannels;
//import org.springframework.integration.event.inbound.ApplicationEventListeningMessageProducer;
//import org.springframework.integration.ftp.server.ApacheMinaFtpEvent;
//import org.springframework.integration.ftp.server.ApacheMinaFtplet;
//import org.springframework.integration.handler.GenericHandler;
//import org.springframework.messaging.MessageChannel;
//
//import java.io.File;
//import java.util.Collections;
//import java.util.Map;
//
//@Slf4j
//@Configuration
//public class FtpServerConfiguration {
//
//    @Bean
//    FileSystemFactory fileSystemFactory(){
//        NativeFileSystemFactory fileSystemFactory = new NativeFileSystemFactory();
//        fileSystemFactory.setCreateHome(true);
//        fileSystemFactory.setCaseInsensitive(false);
//        return fileSystemFactory::createFileSystemView;
//    }
//
//    @Bean
//    FtpServer ftpServer(Map<String, Ftplet> ftpletMap, UserManager userManager, Listener nioListener, FileSystemFactory fileSystemFactory) {
//        FtpServerFactory ftpServerFactory = new FtpServerFactory();
//        ftpServerFactory.setListeners(Collections.singletonMap("default", nioListener));
//        ftpServerFactory.setFileSystem(fileSystemFactory);
//        ftpServerFactory.setFtplets(ftpletMap);
//        ftpServerFactory.setUserManager(userManager);
//        return ftpServerFactory.createServer();
//    }
//
//    @Bean
//    DisposableBean destroyFtpServer(FtpServer ftpServer){
//        return ftpServer::stop;
//    }
//
//
//    @Bean
//    InitializingBean startFtpServer(FtpServer ftpServer){
//        return ftpServer::start;
//    }
//
//    @Bean
//    ApacheMinaFtplet apacheMinaFtplet(){
//        return new ApacheMinaFtplet();
//    }
//
//    @Bean
//    MessageChannel eventsChannel(){
//        return MessageChannels.direct().get();
//    }
//
//    @Bean
//    IntegrationFlow integrationFlow(){
//        return IntegrationFlows.from(this.eventsChannel())
//                .handle((GenericHandler<ApacheMinaFtpEvent>)
//                        (apacheMinaFtpEvent, messageHandlers) -> {
//                    log.info("New event: {} : {}",apacheMinaFtpEvent.getClass().getName(),apacheMinaFtpEvent.getSession());
//                    return null;
//                        })
//                .get();
//    }
//
//    @Bean
//    ApplicationEventListeningMessageProducer applicationEventListeningMessageProducer(){
//        ApplicationEventListeningMessageProducer producer
//                = new ApplicationEventListeningMessageProducer();
//        producer.setEventTypes(ApacheMinaFtpEvent.class);
//        producer.setOutputChannel(eventsChannel());
//        return producer;
//    }
//
//
//
//    @Bean
//    @Scope("prototype")
//    Listener nioListener(){
//        int port = 7777;
//        ListenerFactory listenerFactory = new ListenerFactory();
//        listenerFactory.setPort(port);
//        return listenerFactory.createListener();
//    }
//}
