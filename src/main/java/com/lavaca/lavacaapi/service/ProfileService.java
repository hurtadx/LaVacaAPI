package com.lavaca.lavacaapi.service;

import com.lavaca.lavacaapi.model.Participant;
import com.lavaca.lavacaapi.model.Profile;
import com.lavaca.lavacaapi.repository.ParticipantRepository;
import com.lavaca.lavacaapi.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class ProfileService{

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ParticipantRepository participantRepository;


    @Transactional
    public Profile createProfile(Profile profile) {
        // Validación básica
        if (profile.getEmail() == null || profile.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        if (isEmailInUse(profile.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        if (profile.getUsername() != null && isUsernameInUse(profile.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        // Asignar valores por defecto si no están presentes
        if (profile.getId() == null) {
            profile.setId(UUID.randomUUID());
        }

        if (profile.getCreatedAt() == null) {
            profile.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        return profileRepository.save(profile);
    }



    public Optional<Profile> getProfileById(UUID profileId) {
        return profileRepository.findById(profileId);
    }



    public Optional<Profile> getProfileByEmail(String email) {
        return profileRepository.findByEmail(email);
    }


    public Optional<Profile> getProfileByUsername(String username) {
        return profileRepository.findByUsername(username);
    }


    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }


    @Transactional
    public Profile updateProfile(UUID profileId, Profile updatedProfile) {
        Profile existingProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado con ID: " + profileId));

        // Actualizar campos solo si están presentes en el perfil actualizado
        if (updatedProfile.getUsername() != null) {
            // Verificar que el nuevo nombre de usuario no esté ya en uso por otro perfil
            if (!existingProfile.getUsername().equals(updatedProfile.getUsername()) &&
                    isUsernameInUse(updatedProfile.getUsername())) {
                throw new IllegalArgumentException("El nombre de usuario ya está en uso");
            }
            existingProfile.setUsername(updatedProfile.getUsername());
        }

        // No actualizamos el email aquí, se usa el método específico changeEmail

        // Actualizar timestamp
        existingProfile.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return profileRepository.save(existingProfile);
    }


    @Transactional
    public void deleteProfile(UUID profileId) {
        if (!profileRepository.existsById(profileId)) {
            throw new EntityNotFoundException("Perfil no encontrado con ID: " + profileId);
        }

        profileRepository.deleteById(profileId);
    }


    public List<Profile> getProfilesByVacaId(UUID vacaId) {
        // Obtener IDs de usuarios que participan en una vaca
        List<UUID> userIds = participantRepository.findByVacaId(vacaId)
                .stream()
                .filter(p -> p.getUserId() != null)
                .map(Participant::getUserId)
                .collect(Collectors.toList());

        // Obtener los perfiles correspondientes
        if (userIds.isEmpty()) {
            return List.of();
        }

        return profileRepository.findByUserIdIn(userIds);
    }


    public boolean isEmailInUse(String email) {
        return profileRepository.existsByEmail(email);
    }


    public boolean isUsernameInUse(String username) {
        return profileRepository.existsByUsername(username);
    }


    public List<Profile> searchProfiles(String searchTerm) {
        // Buscamos tanto por nombre de usuario como por email
        List<Profile> byUsername = profileRepository.findByUsernameContainingIgnoreCase(searchTerm);
        List<Profile> byEmail = profileRepository.findByEmailContainingIgnoreCase(searchTerm);

        // Combinamos los resultados y eliminamos duplicados
        return byUsername.stream()
                .filter(profile -> byEmail.stream()
                        .noneMatch(p -> p.getId().equals(profile.getId())))
                .collect(Collectors.toCollection(() -> byEmail));
    }


    public long countProfiles() {
        return profileRepository.count();
    }


    public List<Profile> getProfilesCreatedBetween(Timestamp startDate, Timestamp endDate) {
        return profileRepository.findAll().stream()
                .filter(p -> p.getCreatedAt() != null &&
                        p.getCreatedAt().after(startDate) &&
                        p.getCreatedAt().before(endDate))
                .collect(Collectors.toList());
    }



    public List<Profile> getProfilesByIds(List<UUID> profileIds) {
        return profileRepository.findAllById(profileIds);
    }


    @Transactional
    public Profile updateProfileAvatar(UUID profileId, String avatarUrl) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado con ID: " + profileId));

        // Aquí agregaríamos: profile.setAvatarUrl(avatarUrl);
        // Como no está en el modelo proporcionado, mostramos cómo se implementaría conceptualmente
        // podriamos agg en el futuro para ftos de perfil

        profile.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return profileRepository.save(profile);
    }


    @Transactional
    public Profile changeEmail(UUID profileId, String newEmail) {
        if (newEmail == null || newEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("El nuevo email no puede estar vacío");
        }

        if (isEmailInUse(newEmail)) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado con ID: " + profileId));

        profile.setEmail(newEmail);
        profile.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return profileRepository.save(profile);
    }

    /**
     * Obtiene una versión pública del perfil (solo información que se puede mostrar públicamente)
     * @param profileId ID del perfil
     * @return Mapa con la información pública del perfil
     */
    public Map<String, Object> getPublicProfile(UUID profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado con ID: " + profileId));

        Map<String, Object> publicProfile = new HashMap<>();
        publicProfile.put("id", profile.getId());
        publicProfile.put("username", profile.getUsername());

        // Calculamos estadísticas que queremos mostrar públicamente
        long participationCount = participantRepository.countVacasByUser(profile.getUserId());
        publicProfile.put("participations", participationCount);

        // Añadir fecha de creación si es relevante
        publicProfile.put("memberSince", profile.getCreatedAt());

        return publicProfile;
    }
}
