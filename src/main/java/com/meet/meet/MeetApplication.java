package com.meet.meet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.meet.meet.storage.StorageProperties;
import com.meet.meet.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class MeetApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			// storageService.deleteAll();
			storageService.init();
		};
	}
}
