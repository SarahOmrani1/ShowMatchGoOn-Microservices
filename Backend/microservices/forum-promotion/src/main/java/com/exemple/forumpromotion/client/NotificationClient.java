package com.exemple.forumpromotion.client;

import com.exemple.forumpromotion.dto.NotificationRequestDTO;
import com.exemple.forumpromotion.dto.NotificationResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "content-notification")
public interface NotificationClient {

    @PostMapping("/api/notifications")
    NotificationResponseDTO createNotification(@RequestBody NotificationRequestDTO request);
}
