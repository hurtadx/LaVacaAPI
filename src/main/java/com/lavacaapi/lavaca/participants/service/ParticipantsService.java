package com.lavacaapi.lavaca.participants.service;

import com.lavacaapi.lavaca.participants.Participants;
import com.lavacaapi.lavaca.participants.repository.ParticipantsRepository;
import com.lavacaapi.lavaca.vacas.repository.VacasRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ParticipantsService {

    private final ParticipantsRepository participantsRepository;
    private final VacasRepository vacasRepository;

    @Autowired
    private com.lavacaapi.lavaca.transactions.repository.TransactionsRepository transactionsRepository;

    @Autowired
    private com.lavacaapi.lavaca.profiles.service.ProfilesService profilesService;

    @Autowired
    public ParticipantsService(ParticipantsRepository participantsRepository, VacasRepository vacasRepository) {
        this.participantsRepository = participantsRepository;
        this.vacasRepository = vacasRepository;
    }

    /**
     * Crea un nuevo participante
     * @param participant datos del participante
     * @return participante creado
     */
    @Transactional
    public Participants createParticipant(Participants participant) {
        // Validar datos obligatorios
        if (participant.getVacaId() == null) {
            throw new IllegalArgumentException("El ID de la vaca es obligatorio");
        }

        // Verificar que la vaca exista
        if (!vacasRepository.existsById(participant.getVacaId())) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + participant.getVacaId());
        }

        // Verificar que no exista un participante con el mismo usuario en la misma vaca
        if (participant.getUserId() != null &&
                participantsRepository.existsByVacaIdAndUserId(participant.getVacaId(), participant.getUserId())) {
            throw new IllegalArgumentException("Este usuario ya es participante en esta vaca");
        }

        // Verificar que no exista un participante con el mismo email en la misma vaca
        if (participant.getEmail() != null && !participant.getEmail().trim().isEmpty() &&
                participantsRepository.existsByVacaIdAndEmail(participant.getVacaId(), participant.getEmail())) {
            throw new IllegalArgumentException("Ya existe un participante con este email en esta vaca");
        }

        // Generar ID si no se proporciona
        if (participant.getId() == null) {
            participant.setId(UUID.randomUUID());
        }

        // Establecer fecha de creación si no se proporciona
        if (participant.getCreatedAt() == null) {
            participant.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        // Establecer estado por defecto si no se proporciona
        if (participant.getStatus() == null) {
            participant.setStatus("pendiente");
        }

        return participantsRepository.save(participant);
    }

    /**
     * Bulk invite de participantes
     * @param participants lista de participantes a invitar
     * @return lista de participantes creados
     */
    @Transactional
    public List<Participants> bulkInviteParticipants(List<Participants> participants) {
        return participants.stream().map(this::createParticipant).collect(Collectors.toList());
    }

    /**
     * Obtiene todos los participantes
     * @return lista de participantes
     */
    public List<Participants> getAllParticipants() {
        return participantsRepository.findAll();
    }

    /**
     * Obtiene un participante por su ID
     * @param id ID del participante
     * @return participante encontrado o empty si no existe
     */
    public Optional<Participants> getParticipantById(UUID id) {
        return participantsRepository.findById(id);
    }

    /**
     * Obtiene todos los participantes de una vaca
     * @param vacaId ID de la vaca
     * @return lista de participantes de la vaca
     */
    public List<Participants> getParticipantsByVacaId(UUID vacaId) {
        if (!vacasRepository.existsById(vacaId)) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId);
        }
        List<Participants> participants = participantsRepository.findByVacaId(vacaId);
        // Obtener el owner de la vaca
        var vacaOpt = vacasRepository.findById(vacaId);
        if (vacaOpt.isPresent()) {
            var vaca = vacaOpt.get();
            UUID ownerUserId = vaca.getUserId();
            boolean ownerIsParticipant = participants.stream()
                    .anyMatch(p -> ownerUserId != null && ownerUserId.equals(p.getUserId()));
            if (!ownerIsParticipant && ownerUserId != null) {
                // Crear un participante para el owner, como un participante normal
                Participants owner = new Participants();
                owner.setId(UUID.randomUUID());
                owner.setVacaId(vacaId);
                owner.setUserId(ownerUserId);

                // Obtener la información real del perfil del owner
                var ownerProfile = profilesService.getProfileByUserId(ownerUserId);
                if (ownerProfile.isPresent()) {
                    owner.setName(ownerProfile.get().getUsername());
                    owner.setEmail(ownerProfile.get().getEmail());
                } else {
                    // Si no hay perfil, usar valores neutros
                    owner.setName("Usuario");
                    owner.setEmail(null);
                }

                owner.setCreatedAt(vaca.getCreatedAt());
                owner.setStatus("activo"); // Status normal, sin indicar que es owner
                participants.add(0, owner); // Lo agregamos al inicio
            }
        }
        return participants;
    }

    /**
     * Obtiene todos los participantes de una vaca con paginación
     * @param vacaId ID de la vaca
     * @param pageable información de paginación
     * @return página de participantes
     */
    public Page<Participants> getParticipantsByVacaIdPageable(UUID vacaId, Pageable pageable) {
        if (!vacasRepository.existsById(vacaId)) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId);
        }
        return participantsRepository.findByVacaId(vacaId, pageable);
    }

    /**
     * Obtiene todos los participantes de un usuario
     * @param userId ID del usuario
     * @return lista de participaciones del usuario
     */
    public List<Participants> getParticipantsByUserId(UUID userId) {
        return participantsRepository.findByUserId(userId);
    }

    /**
     * Obtiene todos los participantes con un email específico
     * @param email email a buscar
     * @return lista de participantes con ese email
     */
    public List<Participants> getParticipantsByEmail(String email) {
        return participantsRepository.findByEmail(email);
    }

    /**
     * Obtiene todos los participantes activos de una vaca
     * @param vacaId ID de la vaca
     * @return lista de participantes activos
     */
    public List<Participants> getActiveParticipantsByVacaId(UUID vacaId) {
        if (!vacasRepository.existsById(vacaId)) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId);
        }

        return participantsRepository.findByVacaId(vacaId).stream()
                .filter(p -> "activo".equalsIgnoreCase(p.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los participantes con aportes pendientes de una vaca
     * @param vacaId ID de la vaca
     * @return lista de participantes con aportes pendientes
     */
    public List<Participants> getPendingParticipantsByVacaId(UUID vacaId) {
        if (!vacasRepository.existsById(vacaId)) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId);
        }

        return participantsRepository.findByVacaId(vacaId).stream()
                .filter(p -> "pendiente".equalsIgnoreCase(p.getStatus()))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las contribuciones (transacciones) de un participante
     * @param participantId ID del participante
     * @return historial de contribuciones del participante
     */
    public Map<String, Object> getParticipantContributions(UUID participantId) {
        Participants participant = participantsRepository.findById(participantId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el participante con ID: " + participantId));

        // Obtener todas las transacciones del participante
        List<?> transactions = transactionsRepository.findByParticipantId(participantId);

        // Calcular el total de contribuciones
        BigDecimal totalContributed = BigDecimal.ZERO;

        // Crear el resultado
        Map<String, Object> result = new HashMap<>();
        result.put("participant", participant);
        result.put("transactions", transactions);
        result.put("totalContributed", totalContributed);
        result.put("vacaId", participant.getVacaId());

        return result;
    }

    /**
     * Obtiene participantes con detalles completos
     * @param vacaId ID de la vaca
     * @return lista de participantes con detalles
     */
    @Transactional
    public List<Map<String, Object>> getParticipantsByVacaWithDetails(UUID vacaId) {
        // Usar la lógica de getParticipantsByVacaId para incluir siempre al owner
        List<Participants> participants = getParticipantsByVacaId(vacaId);
        return participants.stream().map(participant -> {
            Map<String, Object> participantDetails = new HashMap<>();
            participantDetails.put("id", participant.getId());
            participantDetails.put("user_id", participant.getUserId());
            participantDetails.put("status", participant.getStatus());
            participantDetails.put("created_at", participant.getCreatedAt());

            // Obtener detalles del perfil del usuario
            if (participant.getUserId() != null) {
                profilesService.getProfileByUserId(participant.getUserId()).ifPresent(profile -> {
                    participantDetails.put("name", profile.getUsername());
                    participantDetails.put("email", profile.getEmail());
                    participantDetails.put("last_activity", profile.getLastActivity());
                });
            }

            return participantDetails;
        }).collect(Collectors.toList());
    }

    /**
     * Actualiza un participante existente
     * @param id ID del participante a actualizar
     * @param participant datos actualizados
     * @return participante actualizado
     */
    @Transactional
    public Participants updateParticipant(UUID id, Participants participant) {
        Participants existing = participantsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el participante con ID: " + id));
        // No permitir cambiar el ID ni la vacaId
        participant.setId(id);
        participant.setVacaId(existing.getVacaId());
        // Preservar fecha de creación
        if (participant.getCreatedAt() == null) {
            participant.setCreatedAt(existing.getCreatedAt());
        }
        // Preservar userId si no se envía
        if (participant.getUserId() == null) {
            participant.setUserId(existing.getUserId());
        }
        // Preservar status si no se envía
        if (participant.getStatus() == null) {
            participant.setStatus(existing.getStatus());
        }
        return participantsRepository.save(participant);
    }

    /**
     * Actualiza el status de un participante
     * @param id ID del participante
     * @param status nuevo status
     * @return participante actualizado
     */
    @Transactional
    public Participants updateParticipantStatus(UUID id, String status) {
        Participants participant = participantsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el participante con ID: " + id));
        participant.setStatus(status);
        return participantsRepository.save(participant);
    }

    /**
     * Elimina un participante
     * @param id ID del participante
     */
    @Transactional
    public void deleteParticipant(UUID id) {
        if (!participantsRepository.existsById(id)) {
            throw new EntityNotFoundException("No se encontró el participante con ID: " + id);
        }
        participantsRepository.deleteById(id);
    }
}
