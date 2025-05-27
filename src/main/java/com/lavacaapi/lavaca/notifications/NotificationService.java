package com.lavacaapi.lavaca.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getNotificationsByUser(UUID userId) {
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> getUnreadNotificationsByUser(UUID userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    public long countUnreadNotifications(UUID userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    public Optional<Notification> getNotificationById(UUID id) {
        return notificationRepository.findById(id);
    }

    @Transactional
    public Notification createNotification(Notification notification) {
        if (notification.getId() == null) {
            notification.setId(UUID.randomUUID());
        }
        if (notification.getCreatedAt() == null) {
            notification.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }
        notification.setRead(false);
        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification markAsRead(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(UUID userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        for (Notification n : notifications) {
            n.setRead(true);
        }
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public void deleteNotification(UUID id) {
        notificationRepository.deleteById(id);
    }
}

