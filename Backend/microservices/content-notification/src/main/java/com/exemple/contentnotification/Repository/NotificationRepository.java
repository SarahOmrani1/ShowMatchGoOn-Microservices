package com.exemple.contentnotification.Repository;

import com.exemple.contentnotification.Entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserId(String userId);
    List<Notification> findByIsReadFalseAndEmailFallbackSentFalseAndEmailFallbackDueAtLessThanEqual(LocalDateTime now);
}
