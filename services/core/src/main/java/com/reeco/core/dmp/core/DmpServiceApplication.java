package com.reeco.core.dmp.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.reeco")
public class DmpServiceApplication {

    private static final Logger LOG = LoggerFactory.getLogger(DmpServiceApplication.class);

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = SpringApplication.run(DmpServiceApplication.class, args);
    }
// export PATH=$PATH:/opt/gradle/gradle-6.4.1/bin
}