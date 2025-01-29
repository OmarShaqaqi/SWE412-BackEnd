package com.backend.senior_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@SpringBootApplication
@RestController
public class SeniorBackendApplication {

	@GetMapping(path = "/hello")
	public String getMethodName() {
		return "Hello, world";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SeniorBackendApplication.class, args);
	}

}
