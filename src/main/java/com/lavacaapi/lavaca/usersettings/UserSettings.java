package com.lavacaapi.lavaca.usersettings;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_settings")
public class UserSettings {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "notifications_enabled", nullable = false)
    private boolean notificationsEnabled = true;

    @Column(name = "privacy", length = 50)
    private String privacy = "default";

    @Column(name = "language", length = 10)
    private String language = "es";

    @Column(name = "timezone", length = 50)
    private String timezone = "America/Mexico_City";

    public UserSettings() {}

    public UserSettings(UUID id, UUID userId, boolean notificationsEnabled, String privacy, String language, String timezone) {
        this.id = id;
        this.userId = userId;
        this.notificationsEnabled = notificationsEnabled;
        this.privacy = privacy;
        this.language = language;
        this.timezone = timezone;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public boolean isNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; }
    public String getPrivacy() { return privacy; }
    public void setPrivacy(String privacy) { this.privacy = privacy; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
}

