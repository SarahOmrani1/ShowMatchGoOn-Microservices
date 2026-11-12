package com.exemple.forumpromotion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ForumPromotionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForumPromotionApplication.class, args);
    }
}
