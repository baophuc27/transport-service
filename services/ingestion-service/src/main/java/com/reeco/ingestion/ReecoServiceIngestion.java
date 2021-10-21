package com.reeco.ingestion;

import com.reeco.ingestion.infrastructure.persistence.cassandra.repository.NumericalTsByOrgRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.cassandra.repository.config.EnableReactiveCassandraRepositories;

@SpringBootApplication
public class ReecoServiceIngestion {
	public static void main(String[] args) {
		SpringApplication.run(ReecoServiceIngestion.class, args);
	}

}
