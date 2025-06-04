package com.lavacaapi.lavaca.profiles.service;

import com.lavacaapi.lavaca.participants.Participants;
import com.lavacaapi.lavaca.profiles.Profiles;
import com.lavacaapi.lavaca.participants.repository.ParticipantsRepository;
import com.lavacaapi.lavaca.profiles.repository.ProfilesRepository;
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
public class ProfilesService {

    @Autowired
    private ProfilesRepository profileRepository;

    @Autowired
    private ParticipantsRepository participantRepository;

    /**
     * Crea un nuevo perfil
     * @param profile datos del perfil
     * @return perfil creado
     */
    @Transactional
    public Profiles createProfile(Profiles profile) {
        if (profile.getEmail() == null || profile.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        if (isEmailInUse(profile.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }
        if (profile.getUsername() != null && isUsernameInUse(profile.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        if (profile.getId() == null) {
            profile.setId(UUID.randomUUID());
        }
        if (profile.getCreatedAt() == null) {
            profile.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }
        return profileRepository.save(profile);
    }

    /**
     * Obtiene un perfil por su ID
     * @param profileId ID del perfil
     * @return perfil encontrado o empty si no existe
     */
    public Optional<Profiles> getProfileById(UUID profileId) {
        return profileRepository.findById(profileId);
    }

    /**
     * Obtiene un perfil por su correo electrónico
     * @param email correo electrónico
     * @return perfil encontrado o empty si no existe
     */
    public Optional<Profiles> getProfileByEmail(String email) {
        return profileRepository.findByEmail(email);
    }

    /**
     * Obtiene un perfil por su nombre de usuario
     * @param username nombre de usuario
     * @return perfil encontrado o empty si no existe
     */
    public Optional<Profiles> getProfileByUsername(String username) {
        return profileRepository.findByUsername(username);
    }

    /**
     * Obtiene un perfil por su ID de usuario
     * @param userId ID del usuario
     * @return perfil encontrado o empty si no existe
     */
    public Optional<Profiles> getProfileByUserId(UUID userId) {
        return profileRepository.findByUserId(userId);
    }

    /**
     * Obtiene todos los perfiles
     * @return lista de perfiles
     */
    public List<Profiles> getAllProfiles() {
        return profileRepository.findAll();
    }

    /**
     * Actualiza un perfil existente
     * @param profileId ID del perfil a actualizar
     * @param updatedProfile datos actualizados
     * @return perfil actualizado
     */
    @Transactional
    public Profiles updateProfile(UUID profileId, Profiles updatedProfile) {
        Profiles existingProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado con ID: " + profileId));

        if (updatedProfile.getUsername() != null) {
            if (!existingProfile.getUsername().equals(updatedProfile.getUsername()) &&
                    isUsernameInUse(updatedProfile.getUsername())) {
                throw new IllegalArgumentException("El nombre de usuario ya está en uso");
            }
            existingProfile.setUsername(updatedProfile.getUsername());
        }

        if (updatedProfile.getEmail() != null) {
            if (!existingProfile.getEmail().equals(updatedProfile.getEmail()) &&
                    isEmailInUse(updatedProfile.getEmail())) {
                throw new IllegalArgumentException("El email ya está en uso");
            }
            existingProfile.setEmail(updatedProfile.getEmail());
        }

        existingProfile.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return profileRepository.save(existingProfile);
    }

    /**
     * Elimina un perfil
     * @param profileId ID del perfil a eliminar
     */
    @Transactional
    public void deleteProfile(UUID profileId) {
        if (!profileRepository.existsById(profileId)) {
            throw new EntityNotFoundException("Perfil no encontrado con ID: " + profileId);
        }
        profileRepository.deleteById(profileId);
    }

    /**
     * Obtiene todos los perfiles relacionados con una vaca
     * @param vacaId ID de la vaca
     * @return lista de perfiles de los participantes de la vaca
     */
    public List<Profiles> getProfilesByVacaId(UUID vacaId) {
        List<UUID> userIds = participantRepository.findByVacaId(vacaId)
                .stream()
                .filter(p -> p.getUserId() != null)
                .map(Participants::getUserId)
                .collect(Collectors.toList());

        if (userIds.isEmpty()) {
            return List.of();
        }

        return profileRepository.findByUserIdIn(userIds);
    }

    /**
     * Verifica si un correo electrónico ya está en uso
     * @param email correo a verificar
     * @return true si ya existe un perfil con ese email, false en caso contrario
     */
    public boolean isEmailInUse(String email) {
        return profileRepository.existsByEmail(email);
    }

    /**
     * Verifica si un nombre de usuario ya está en uso
     * @param username nombre de usuario a verificar
     * @return true si ya existe un perfil con ese nombre de usuario, false en caso contrario
     */
    public boolean isUsernameInUse(String username) {
        return profileRepository.existsByUsername(username);
    }

    /**
     * Busca perfiles por nombre de usuario o correo electrónico
     * @param searchTerm término de búsqueda
     * @return lista de perfiles que coinciden con la búsqueda
     */
    public List<Profiles> searchProfiles(String searchTerm) {
        List<Profiles> byUsername = profileRepository.findByUsernameContainingIgnoreCase(searchTerm);
        List<Profiles> byEmail = profileRepository.findByEmailContainingIgnoreCase(searchTerm);

        // Combinar resultados y eliminar duplicados
        return byUsername.stream()
                .filter(profile -> byEmail.stream()
                        .noneMatch(p -> p.getId().equals(profile.getId())))
                .collect(Collectors.toCollection(() -> byEmail));
    }

    /**
     * Cuenta el número total de perfiles
     * @return cantidad de perfiles
     */
    public long countProfiles() {
        return profileRepository.count();
    }

    /**
     * Obtiene perfiles creados en un rango de fechas
     * @param startDate fecha inicial
     * @param endDate fecha final
     * @return lista de perfiles en el rango especificado
     */
    public List<Profiles> getProfilesCreatedBetween(Timestamp startDate, Timestamp endDate) {
        return profileRepository.findAll().stream()
                .filter(p -> p.getCreatedAt() != null &&
                        p.getCreatedAt().after(startDate) &&
                        p.getCreatedAt().before(endDate))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene perfiles por sus IDs
     * @param profileIds lista de IDs de perfiles
     * @return lista de perfiles encontrados
     */
    public List<Profiles> getProfilesByIds(List<UUID> profileIds) {
        return profileRepository.findAllById(profileIds);
    }

    /**
     * Cambia el correo electrónico de un perfil
     * @param profileId ID del perfil
     * @param newEmail nuevo correo electrónico
     * @return perfil actualizado
     */
    @Transactional
    public Profiles changeEmail(UUID profileId, String newEmail) {
        if (newEmail == null || newEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("El nuevo email no puede estar vacío");
        }

        if (isEmailInUse(newEmail)) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        Profiles profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado con ID: " + profileId));

        profile.setEmail(newEmail);
        profile.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return profileRepository.save(profile);
    }

    /**
     * Obtiene la información pública de un perfil
     * @param profileId ID del perfil
     * @return mapa con la información pública del perfil
     */
    public Map<String, Object> getPublicProfile(UUID profileId) {
        Profiles profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new EntityNotFoundException("Perfil no encontrado con ID: " + profileId));

        Map<String, Object> publicProfile = new HashMap<>();
        publicProfile.put("id", profile.getId());
        publicProfile.put("username", profile.getUsername());

        // Contar en cuántas vacas participa el usuario
        long participationCount = participantRepository.countVacasByUser(profile.getUserId());
        publicProfile.put("participations", participationCount);

        // Añadir fecha de registro
        publicProfile.put("memberSince", profile.getCreatedAt());

        return publicProfile;
    }
}
