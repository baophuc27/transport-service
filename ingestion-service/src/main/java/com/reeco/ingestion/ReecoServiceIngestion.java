package com.reeco.ingestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReecoServiceIngestion {
	public static void main(String[] args) {
		SpringApplication.run(ReecoServiceIngestion.class, args);
	}

}
