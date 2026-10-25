package com.exemple.contentnotification.Service.impl;

import com.exemple.contentnotification.dto.NotificationDTO;
import com.exemple.contentnotification.Entity.Notification;
import com.exemple.contentnotification.exception.ResourceNotFoundException;
import com.exemple.contentnotification.Repository.NotificationRepository;
import com.exemple.contentnotification.Service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Notification Service Implementation
 * WHY: Users live in the user service, so a notification carries the recipient's
 * userId/username/email itself; unread notifications are emailed after a delay
 */
@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   JavaMailSender mailSender) {
        this.notificationRepository = notificationRepository;
        this.mailSender = mailSender;
    }

    @Value("${app.notifications.email-fallback-delay-seconds:300}")
    private long emailFallbackDelaySeconds;

    @Value("${app.notifications.email-fallback-enabled:true}")
    private boolean emailFallbackEnabled;

    @Value("${app.notifications.email-from:no-reply@smgo.local}")
    private String fromEmail;

    @Override
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        try {
            log.info("Creating notification for user: {}", notificationDTO.getUserId());

            Notification notification = Notification.builder()
                    .message(notificationDTO.getMessage())
                    .title(notificationDTO.getTitle())
                    .type(notificationDTO.getType())
                    .createdAt(LocalDateTime.now())
                    .emailFallbackDueAt(LocalDateTime.now().plusSeconds(emailFallbackDelaySeconds))
                    .isRead(false)
                    .emailFallbackSent(false)
                    .userId(notificationDTO.getUserId())
                    .username(notificationDTO.getUsername())
                    .email(notificationDTO.getEmail())
                    .build();

            Notification savedNotification = notificationRepository.save(notification);
            log.info("Notification saved successfully with ID: {}", savedNotification.getId());
            return mapToDTO(savedNotification);
        } catch (Exception e) {
            log.error("Error creating notification: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create notification: " + e.getMessage(), e);
        }
    }

    @Override
    public List<NotificationDTO> getNotificationsByUserId(String userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id));
        notification.setIsRead(true);
        notification.setEmailFallbackSent(true);
        notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(String id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id));
        notificationRepository.delete(notification);
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        if (!isDeliverableEmail(to)) {
            log.warn("Skipping email send because recipient is not deliverable: {}", to);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(fromEmail, "SMGO admin");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);
            mailSender.send(message);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to send email to " + to, ex);
        }
        log.info("Email sent successfully to {}", to);
    }

    @Override
    public void sendSms(String phoneNumber, String message) {
        log.info("Sending SMS to: {}, Message: {}", phoneNumber, message);
        // Structure for real API integration
    }

    @Scheduled(fixedDelayString = "${app.notifications.email-fallback-check-interval-ms:10000}")
    public void processUnreadNotificationEmailFallback() {
        int processed = processEmailFallbackNow();
        if (processed > 0) {
            log.info("Notification fallback scheduler completed: {} email fallback(s) processed", processed);
        }
    }

    @Override
    public int processEmailFallbackNow() {
        if (!emailFallbackEnabled) {
            return 0;
        }

        LocalDateTime now = LocalDateTime.now();
        List<Notification> pending = notificationRepository
                .findByIsReadFalseAndEmailFallbackSentFalseAndEmailFallbackDueAtLessThanEqual(now);

        if (pending.isEmpty()) {
            return 0;
        }

        int processed = 0;

        for (Notification notification : pending) {
            String email = notification.getEmail();
            if (!isDeliverableEmail(email)) {
                notification.setEmailFallbackSent(true);
                notification.setEmailFallbackSentAt(now);
                notificationRepository.save(notification);
                processed++;
                continue;
            }

            String subject = notification.getTitle() != null && !notification.getTitle().isBlank()
                    ? notification.getTitle()
                    : "SMGO Notification";
            try {
                sendEmail(email, subject, notification.getMessage());
                notification.setEmailFallbackSent(true);
                notification.setEmailFallbackSentAt(now);
                notificationRepository.save(notification);
                processed++;
            } catch (Exception ex) {
                log.error("Failed to send fallback email for notification {}: {}", notification.getId(), ex.getMessage(), ex);
            }
        }

        return processed;
    }

    private boolean isDeliverableEmail(String email) {
        return email != null && !email.isBlank() && !email.endsWith("@system.local");
    }

    private NotificationDTO mapToDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .title(notification.getTitle())
                .type(notification.getType())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.getIsRead())
                .userId(notification.getUserId())
                .username(notification.getUsername())
                .email(notification.getEmail())
                .build();
    }
}
