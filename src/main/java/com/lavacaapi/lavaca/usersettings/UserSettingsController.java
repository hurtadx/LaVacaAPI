package com.lavacaapi.lavaca.usersettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/user-settings")
public class UserSettingsController {

    @Autowired
    private UserSettingsService userSettingsService;

    // Obtener configuraciones de usuario
    @GetMapping("/{userId}")
    public ResponseEntity<UserSettings> getUserSettings(@PathVariable UUID userId) {
        Optional<UserSettings> settings = userSettingsService.getUserSettings(userId);
        return settings.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Actualizar preferencias de notificaciones
    @PutMapping("/{userId}/notifications")
    public ResponseEntity<UserSettings> updateNotifications(@PathVariable UUID userId, @RequestBody boolean enabled) {
        return ResponseEntity.ok(userSettingsService.updateNotifications(userId, enabled));
    }

    // Actualizar privacidad
    @PutMapping("/{userId}/privacy")
    public ResponseEntity<UserSettings> updatePrivacy(@PathVariable UUID userId, @RequestBody String privacy) {
        return ResponseEntity.ok(userSettingsService.updatePrivacy(userId, privacy));
    }

    // Actualizar idioma
    @PutMapping("/{userId}/language")
    public ResponseEntity<UserSettings> updateLanguage(@PathVariable UUID userId, @RequestBody String language) {
        return ResponseEntity.ok(userSettingsService.updateLanguage(userId, language));
    }

    // Actualizar zona horaria
    @PutMapping("/{userId}/timezone")
    public ResponseEntity<UserSettings> updateTimezone(@PathVariable UUID userId, @RequestBody String timezone) {
        return ResponseEntity.ok(userSettingsService.updateTimezone(userId, timezone));
    }
}

