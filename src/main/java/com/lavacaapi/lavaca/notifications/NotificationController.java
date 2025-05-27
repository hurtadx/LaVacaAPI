package com.lavacaapi.lavaca.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Obtener todas las notificaciones de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUser(userId));
    }

    // Obtener solo las no leídas
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotificationsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotificationsByUser(userId));
    }

    // Contar no leídas
    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Long> countUnreadNotifications(@PathVariable UUID userId) {
        return ResponseEntity.ok(notificationService.countUnreadNotifications(userId));
    }

    // Crear notificación (admin o sistema)
    @PostMapping("/send")
    public ResponseEntity<Notification> sendNotification(@RequestBody Notification notification) {
        return new ResponseEntity<>(notificationService.createNotification(notification), HttpStatus.CREATED);
    }

    // Marcar una notificación como leída
    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable UUID id) {
        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    // Marcar todas como leídas para un usuario
    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable UUID userId) {
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    // Eliminar una notificación
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable UUID id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener una notificación por ID
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable UUID id) {
        Optional<Notification> notification = notificationService.getNotificationById(id);
        return notification.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}

