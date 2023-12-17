package com.reeco.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@ComponentScan({"com.reeco"})
public class TransportHTTPApplication {
    private static final Logger LOG = LoggerFactory.getLogger(TransportHTTPApplication.class);

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = SpringApplication.run(TransportHTTPApplication.class, args);
    }
    @PostConstruct
    public void init(){
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
    }
}

