package com.backend.senior_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;



@SpringBootApplication(exclude = {ReactiveSecurityAutoConfiguration.class })
@RestController
public class SeniorBackendApplication {

	
	
	public static void main(String[] args) {
		SpringApplication.run(SeniorBackendApplication.class, args);
	}

}
