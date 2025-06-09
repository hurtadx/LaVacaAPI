package com.lavacaapi.lavaca.invitations.controller;

import com.lavacaapi.lavaca.invitations.Invitations;
import com.lavacaapi.lavaca.invitations.service.InvitationsService;
import com.lavacaapi.lavaca.participants.Participants;
import com.lavacaapi.lavaca.participants.service.ParticipantsService;
import com.lavacaapi.lavaca.invitations.controller.dto.InvitationRequestDTO;
import com.lavacaapi.lavaca.participants.dto.ParticipantRequestDTO;
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

    // Obtener invitaciones recibidas por un usuario
    @GetMapping("/user/{userId}/received")
    public ResponseEntity<List<Invitations>> getReceivedInvitations(@PathVariable UUID userId) {
        List<Invitations> invitations = invitationsService.getInvitationsByReceiverId(userId);
        return ResponseEntity.ok(invitations);
    }

    // Enviar invitación para agregar participante a una vaca
    @PostMapping
    public ResponseEntity<Invitations> sendInvitation(@RequestBody InvitationRequestDTO invitationRequestDTO) {
        try {
            Invitations invitation = invitationsService.createInvitation(invitationRequestDTO);
            return new ResponseEntity<>(invitation, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Log para depuración
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Aceptar invitación
    @PutMapping("/{invitationId}/accept")
    public ResponseEntity<Invitations> acceptInvitation(@PathVariable UUID invitationId, @RequestBody ParticipantRequestDTO participantRequestDTO) {
        try {
            Invitations updated = invitationsService.updateInvitationStatus(invitationId, "accepted", participantRequestDTO);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Rechazar invitación
    @PutMapping("/{invitationId}/reject")
    public ResponseEntity<Invitations> rejectInvitation(@PathVariable UUID invitationId, @RequestBody(required = false) ParticipantRequestDTO participantRequestDTO) {
        try {
            Invitations updated = invitationsService.updateInvitationStatus(invitationId, "rejected", participantRequestDTO);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

