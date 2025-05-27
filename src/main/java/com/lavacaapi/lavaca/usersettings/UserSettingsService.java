package com.lavacaapi.lavaca.usersettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserSettingsService {
    @Autowired
    private UserSettingsRepository userSettingsRepository;

    public Optional<UserSettings> getUserSettings(UUID userId) {
        return userSettingsRepository.findByUserId(userId);
    }

    @Transactional
    public UserSettings updateNotifications(UUID userId, boolean enabled) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Configuración no encontrada para el usuario"));
        settings.setNotificationsEnabled(enabled);
        return userSettingsRepository.save(settings);
    }

    @Transactional
    public UserSettings updatePrivacy(UUID userId, String privacy) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Configuración no encontrada para el usuario"));
        settings.setPrivacy(privacy);
        return userSettingsRepository.save(settings);
    }

    @Transactional
    public UserSettings updateLanguage(UUID userId, String language) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Configuración no encontrada para el usuario"));
        settings.setLanguage(language);
        return userSettingsRepository.save(settings);
    }

    @Transactional
    public UserSettings updateTimezone(UUID userId, String timezone) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Configuración no encontrada para el usuario"));
        settings.setTimezone(timezone);
        return userSettingsRepository.save(settings);
    }
}

