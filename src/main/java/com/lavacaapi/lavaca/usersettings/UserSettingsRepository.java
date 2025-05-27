package com.lavacaapi.lavaca.usersettings;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserSettingsRepository extends JpaRepository<UserSettings, UUID> {
    Optional<UserSettings> findByUserId(UUID userId);
}

