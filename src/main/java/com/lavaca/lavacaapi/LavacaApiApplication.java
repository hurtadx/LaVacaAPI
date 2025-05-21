package com.lavaca.lavacaapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.lavaca.lavacaapi.model")
@EnableJpaRepositories("com.lavaca.lavacaapi.repository")
public class LavacaApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(LavacaApiApplication.class, args);
	}
}
