package com.example.recruiting_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication(scanBasePackages = "com.example.recruiting_application")
@CrossOrigin(origins = "http://127.0.0.1:5501")// Adjust the package as necessary
public class RecruitingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecruitingApplication.class, args);
	}
}





