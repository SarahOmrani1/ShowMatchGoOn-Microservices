package com.exemple.cinemareservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {
		"com.exemple.cinemareservation",
		"controller",
		"service",
		"repository",
		"entity",
		"exception"
})
@EnableDiscoveryClient
@EnableMongoRepositories(basePackages = "repository")
public class CinemaReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaReservationApplication.class, args);
	}

}
