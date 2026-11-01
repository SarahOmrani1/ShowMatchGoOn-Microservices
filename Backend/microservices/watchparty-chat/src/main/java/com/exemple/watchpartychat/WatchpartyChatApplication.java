package com.exemple.watchpartychat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {
		"com.exemple.watchpartychat",
		"controller", "service", "entity", "repository", "config", "client"
})
@EnableMongoRepositories(basePackages = "repository")
@EntityScan(basePackages = "entity")
@EnableFeignClients(basePackages = "client")
public class WatchpartyChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(WatchpartyChatApplication.class, args);
	}

}
