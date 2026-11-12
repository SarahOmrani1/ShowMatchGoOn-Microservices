package com.exemple.subscriptionloyalty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.exemple.subscriptionloyalty.Client")
public class SubscriptionLoyaltyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionLoyaltyApplication.class, args);
	}

}
