package com.lavacaapi.lavaca.invitations.controller;

import com.lavacaapi.lavaca.invitations.Invitations;
import com.lavacaapi.lavaca.invitations.service.InvitationsService;
import com.lavacaapi.lavaca.participants.Participants;
import com.lavacaapi.lavaca.participants.service.ParticipantsService;
import com.lavacaapi.lavaca.invitations.controller.dto.InvitationRequestDTO;
import com.lavacaapi.lavaca.participants.dto.ParticipantRequestDTO;
import com.lavacaapi.lavaca.notifications.Notification;
import com.lavacaapi.lavaca.notifications.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/invitations")
public class InvitationsController {

    @Autowired
    private InvitationsService invitationsService;

    @Autowired
    private NotificationService notificationService;

    // Obtener invitaciones recibidas por un usuario
    @GetMapping("/user/{userId}/received")
    public ResponseEntity<List<Invitations>> getReceivedInvitations(@PathVariable UUID userId, @RequestParam(required = false) String status) {
        List<Invitations> invitations;
        if (status != null && !status.isEmpty()) {
            invitations = invitationsService.getInvitationsByReceiverIdAndStatus(userId, status);
        } else {
            invitations = invitationsService.getInvitationsByReceiverId(userId);
        }
        return ResponseEntity.ok(invitations);
    }

    // Enviar invitación para agregar participante a una vaca
    @PostMapping
    public ResponseEntity<List<Invitations>> sendInvitation(@RequestBody InvitationRequestDTO invitationRequestDTO) {
        List<Invitations> createdInvitations = new ArrayList<>();
        for (UUID receiverId : invitationRequestDTO.getUser_ids()) {
            // Validación de campos obligatorios
            if (invitationRequestDTO.getVaca_id() == null || invitationRequestDTO.getSender_id() == null || receiverId == null) {
                throw new IllegalArgumentException("Faltan datos obligatorios para la invitación (vacaId, senderId o receiverId)");
            }
            Invitations invitation = new Invitations();
            invitation.setVacaId(invitationRequestDTO.getVaca_id());
            invitation.setSenderId(invitationRequestDTO.getSender_id());
            invitation.setReceiverId(receiverId);
            invitation.setStatus("pending");
            invitation.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            Invitations savedInvitation = invitationsService.createInvitation(invitation);
            createdInvitations.add(savedInvitation);
            // Crear notificación para el usuario invitado
            Notification notification = new Notification();
            notification.setUserId(receiverId);
            notification.setMessage("Has sido invitado a una vaca");
            notification.setType("invitation");
            notificationService.createNotification(notification);
        }
        return new ResponseEntity<>(createdInvitations, HttpStatus.CREATED);
    }

    // Aceptar invitación
    @PutMapping("/{invitationId}/accept")
    public ResponseEntity<Invitations> acceptInvitation(@PathVariable UUID invitationId) {
        try {
            Invitations updated = invitationsService.updateInvitationStatus(invitationId, "ACCEPTED");
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Rechazar invitación
    @PutMapping("/{invitationId}/reject")
    public ResponseEntity<Invitations> rejectInvitation(@PathVariable UUID invitationId) {
        try {
            Invitations updated = invitationsService.updateInvitationStatus(invitationId, "REJECTED");
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

