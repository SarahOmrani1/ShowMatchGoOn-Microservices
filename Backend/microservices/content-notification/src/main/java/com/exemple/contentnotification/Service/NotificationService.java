package com.exemple.contentnotification.Service;

import com.exemple.contentnotification.dto.NotificationDTO;
import com.exemple.contentnotification.dto.SanctionDTO;
import java.util.List;

public interface NotificationService {
    NotificationDTO createNotification(NotificationDTO notificationDTO);
    List<NotificationDTO> getAllNotifications();
    List<NotificationDTO> getNotificationsByUserId(String userId);
    void markAsRead(String id);
    void deleteNotification(String id);
    int processEmailFallbackNow();

    // ==================== COMMUNICATION SYNCHRONE : moderation (OpenFeign) ====================
    List<SanctionDTO> getSanctionsByUser(Long userId);
    NotificationDTO notifyLatestSanction(Long userId);

    // External notification structure
    void sendEmail(String to, String subject, String body);
    void sendSms(String phoneNumber, String message);
}
