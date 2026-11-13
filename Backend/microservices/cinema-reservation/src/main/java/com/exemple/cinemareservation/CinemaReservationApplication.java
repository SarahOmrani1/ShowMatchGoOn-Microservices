package com.exemple.cinemareservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {
		"com.exemple.cinemareservation",
		"controller",
		"service",
		"repository",
		"exception",
		"client",
		"dto",
		"entity"
})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "client")
@EnableMongoRepositories(basePackages = "repository")
public class CinemaReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaReservationApplication.class, args);
	}

}
