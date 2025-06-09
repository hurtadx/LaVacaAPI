package com.lavacaapi.lavaca.invitations.service;

import com.lavacaapi.lavaca.invitations.Invitations;
import com.lavacaapi.lavaca.invitations.repository.InvitationsRepository;
import com.lavacaapi.lavaca.vacas.repository.VacasRepository;
import com.lavacaapi.lavaca.participants.Participants;
import com.lavacaapi.lavaca.participants.service.ParticipantsService;
import com.lavacaapi.lavaca.notifications.Notification;
import com.lavacaapi.lavaca.notifications.NotificationService;
import com.lavacaapi.lavaca.profiles.service.ProfilesService;
import com.lavacaapi.lavaca.profiles.Profiles;
import com.lavacaapi.lavaca.invitations.controller.dto.InvitationRequestDTO;
import com.lavacaapi.lavaca.participants.dto.ParticipantRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvitationsService {

    @Autowired
    private InvitationsRepository invitationsRepository;

    @Autowired
    private VacasRepository vacasRepository;

    @Autowired
    private ParticipantsService participantsService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ProfilesService profilesService;

    /**
     * Crea una nueva invitación
     * @param invitation datos de la invitación
     * @return invitación creada
     */
    @Transactional
    public Invitations createInvitation(Invitations invitation) {
        // Validar datos obligatorios
        if (invitation.getVacaId() == null) {
            throw new IllegalArgumentException("El ID de la vaca es obligatorio");
        }

        if (invitation.getSenderId() == null) {
            throw new IllegalArgumentException("El ID del remitente es obligatorio");
        }

        // Verificar que la vaca exista
        if (!vacasRepository.existsById(invitation.getVacaId())) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + invitation.getVacaId());
        }

        // Validar que el usuario receptor exista antes de crear la invitación y la notificación
        boolean receptorExiste = profilesService.getProfileByUserId(invitation.getReceiverId()).isPresent();
        if (!receptorExiste) {
            throw new EntityNotFoundException("El usuario receptor no existe: " + invitation.getReceiverId());
        }

        // Verificar que no exista una invitación pendiente para la misma vaca y receptor
        boolean existePendiente = invitationsRepository.findByReceiverIdAndVacaId(invitation.getReceiverId(), invitation.getVacaId())
            .stream()
            .anyMatch(inv -> "pending".equalsIgnoreCase(inv.getStatus()));
        if (existePendiente) {
            String nombre = profilesService
                .getProfileByUserId(invitation.getReceiverId())
                .map(Profiles::getUsername)
                .orElse("El usuario");
            throw new IllegalArgumentException(nombre + " ya tiene una invitación pendiente a esta vaca");
        }

        // Generar ID si no se proporciona
        if (invitation.getId() == null) {
            invitation.setId(UUID.randomUUID());
        }

        // Establecer fecha de creación si no se proporciona
        if (invitation.getCreatedAt() == null) {
            invitation.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        // Establecer estado por defecto si no se proporciona
        if (invitation.getStatus() == null) {
            invitation.setStatus("pending");
        }

        Invitations savedInvitation = invitationsRepository.save(invitation);

        // Enviar notificación al receptor
        Notification notification = new Notification();
        notification.setUserId(invitation.getReceiverId());
        notification.setMessage("Has recibido una nueva invitación para unirte a una vaca.");
        notification.setType("INVITATION");
        notificationService.createNotification(notification);

        return savedInvitation;
    }

    /**
     * Crea invitaciones a partir de un InvitationRequestDTO
     * @param dto datos de la invitación
     * @return la primera invitación creada (puedes ajustar para retornar una lista si lo prefieres)
     */
    @Transactional
    public Invitations createInvitation(InvitationRequestDTO dto) {
        Invitations lastInvitation = null;
        for (UUID userId : dto.getUser_ids()) {
            Invitations invitation = new Invitations();
            invitation.setVacaId(dto.getVaca_id());
            invitation.setReceiverId(userId);
            invitation.setSenderId(dto.getSender_id());
            invitation.setStatus("pending");
            invitation.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            lastInvitation = createInvitation(invitation);
        }
        return lastInvitation;
    }

    /**
     * Obtiene todas las invitaciones
     * @return lista de invitaciones
     */
    public List<Invitations> getAllInvitations() {
        return invitationsRepository.findAll();
    }

    /**
     * Obtiene una invitación por su ID
     * @param id ID de la invitación
     * @return invitación encontrada o empty si no existe
     */
    public Optional<Invitations> getInvitationById(UUID id) {
        return invitationsRepository.findById(id);
    }

    /**
     * Obtiene todas las invitaciones relacionadas con una vaca
     * @param vacaId ID de la vaca
     * @return lista de invitaciones de la vaca
     */
    public List<Invitations> getInvitationsByVacaId(UUID vacaId) {
        if (!vacasRepository.existsById(vacaId)) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId);
        }
        return invitationsRepository.findByVacaId(vacaId);
    }

    /**
     * Obtiene todas las invitaciones enviadas por un remitente
     * @param senderId ID del remitente
     * @return lista de invitaciones del remitente
     */
    public List<Invitations> getInvitationsBySenderId(UUID senderId) {
        return invitationsRepository.findBySenderId(senderId);
    }

    /**
     * Obtiene todas las invitaciones recibidas por un usuario
     * @param receiverId ID del usuario receptor
     * @return lista de invitaciones recibidas
     */
    public List<Invitations> getInvitationsByReceiverId(UUID receiverId) {
        return invitationsRepository.findByReceiverId(receiverId);
    }

    /**
     * Obtiene todas las invitaciones con un estado específico
     * @param status estado de la invitación (PENDING, ACCEPTED, REJECTED)
     * @return lista de invitaciones con el estado especificado
     */
    public List<Invitations> getInvitationsByStatus(String status) {
        return invitationsRepository.findByStatus(status);
    }

    /**
     * Actualiza una invitación existente
     * @param id ID de la invitación a actualizar
     * @param invitation Datos actualizados
     * @return invitación actualizada
     */
    @Transactional
    public Invitations updateInvitation(UUID id, Invitations invitation) {
        Invitations existingInvitation = invitationsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la invitación con ID: " + id));

        // No permitir cambiar el ID, vacaId o senderId
        invitation.setId(id);
        invitation.setVacaId(existingInvitation.getVacaId());
        invitation.setSenderId(existingInvitation.getSenderId());

        // Preservar fecha de creación
        if (invitation.getCreatedAt() == null) {
            invitation.setCreatedAt(existingInvitation.getCreatedAt());
        }

        return invitationsRepository.save(invitation);
    }

    /**
     * Actualiza el estado de una invitación (aceptar, rechazar, cancelar)
     * @param id ID de la invitación
     * @param status nuevo estado (pending, accepted, rejected, cancelled)
     * @param participantRequestDTO datos del participante
     * @return invitación actualizada
     */
    @Transactional
    public Invitations updateInvitationStatus(UUID id, String status, ParticipantRequestDTO participantRequestDTO) {
        if (status == null || (!status.equals("pending") && !status.equals("accepted") && !status.equals("rejected") && !status.equals("cancelled"))) {
            throw new IllegalArgumentException("El estado debe ser pending, accepted, rejected o cancelled");
        }
        Invitations invitation = invitationsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la invitación con ID: " + id));
        invitation.setStatus(status);
        Invitations updatedInvitation = invitationsRepository.save(invitation);
        // Si el nuevo estado es accepted, agregar como participante usando los datos del body
        if ("accepted".equals(status)) {
            Participants participant = new Participants();
            participant.setVacaId(invitation.getVacaId());
            participant.setUserId(participantRequestDTO.getUser_id());
            participant.setName(participantRequestDTO.getName());
            participant.setEmail(participantRequestDTO.getEmail());
            participant.setStatus("activo");
            participant.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            participantsService.createParticipant(participant);
        }
        return updatedInvitation;
    }

    /**
     * Elimina una invitación
     * @param id ID de la invitación a eliminar
     */
    @Transactional
    public void deleteInvitation(UUID id) {
        if (!invitationsRepository.existsById(id)) {
            throw new EntityNotFoundException("No se encontró la invitación con ID: " + id);
        }
        invitationsRepository.deleteById(id);
    }
}

