package com.exemple.watchpartychat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {"com.exemple.watchpartychat", "controller", "service", "repository", "entity"})
@EnableMongoRepositories(basePackages = "repository")
@EntityScan(basePackages = "entity")
public class WatchpartyChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(WatchpartyChatApplication.class, args);
	}
}