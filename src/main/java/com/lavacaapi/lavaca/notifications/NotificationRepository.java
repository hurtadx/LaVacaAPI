package com.lavacaapi.lavaca.notifications;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserId(UUID userId);
    List<Notification> findByUserIdAndIsReadFalse(UUID userId);
    long countByUserIdAndIsReadFalse(UUID userId);
}

