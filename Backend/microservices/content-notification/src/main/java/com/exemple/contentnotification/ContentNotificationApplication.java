package com.exemple.contentnotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class ContentNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContentNotificationApplication.class, args);
	}

}
