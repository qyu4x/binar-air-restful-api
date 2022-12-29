package com.binarair.binarairrestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class BinarAirRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BinarAirRestApiApplication.class, args);
	}

}
