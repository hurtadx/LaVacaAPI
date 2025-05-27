package com.lavacaapi.lavaca.invitations.controller;

import com.lavacaapi.lavaca.invitations.Invitations;
import com.lavacaapi.lavaca.invitations.service.InvitationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/invitations")
public class InvitationsController {

    @Autowired
    private InvitationsService invitationsService;

    @PostMapping
    public ResponseEntity<Invitations> createInvitation(@RequestBody Invitations invitation) {
        return new ResponseEntity<>(invitationsService.createInvitation(invitation), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Invitations>> getAllInvitations() {
        return ResponseEntity.ok(invitationsService.getAllInvitations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invitations> getInvitationById(@PathVariable UUID id) {
        Optional<Invitations> invitation = invitationsService.getInvitationById(id);
        return invitation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/vaca/{vacaId}")
    public ResponseEntity<List<Invitations>> getInvitationsByVaca(@PathVariable UUID vacaId) {
        return ResponseEntity.ok(invitationsService.getInvitationsByVacaId(vacaId));
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<Invitations>> getInvitationsBySender(@PathVariable UUID senderId) {
        return ResponseEntity.ok(invitationsService.getInvitationsBySenderId(senderId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Invitations>> getInvitationsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(invitationsService.getInvitationsByStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Invitations> updateInvitation(@PathVariable UUID id, @RequestBody Invitations invitation) {
        return ResponseEntity.ok(invitationsService.updateInvitation(id, invitation));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Invitations> updateInvitationStatus(
            @PathVariable UUID id,
            @RequestBody String status) {
        return ResponseEntity.ok(invitationsService.updateInvitationStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvitation(@PathVariable UUID id) {
        invitationsService.deleteInvitation(id);
        return ResponseEntity.noContent().build();
    }

    // Invitaciones recibidas por usuario
    @GetMapping("/user/{userId}/received")
    public ResponseEntity<List<Invitations>> getInvitationsReceived(@PathVariable UUID userId) {
        return ResponseEntity.ok(invitationsService.getInvitationsByReceiverId(userId));
    }

    // Invitaciones enviadas por usuario
    @GetMapping("/user/{userId}/sent")
    public ResponseEntity<List<Invitations>> getInvitationsSent(@PathVariable UUID userId) {
        return ResponseEntity.ok(invitationsService.getInvitationsBySenderId(userId));
    }

    // Aceptar invitación
    @PutMapping("/{id}/accept")
    public ResponseEntity<Invitations> acceptInvitation(@PathVariable UUID id) {
        return ResponseEntity.ok(invitationsService.updateInvitationStatus(id, "ACCEPTED"));
    }

    // Rechazar invitación
    @PutMapping("/{id}/reject")
    public ResponseEntity<Invitations> rejectInvitation(@PathVariable UUID id) {
        return ResponseEntity.ok(invitationsService.updateInvitationStatus(id, "REJECTED"));
    }

    // Cancelar invitación enviada
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Invitations> cancelInvitation(@PathVariable UUID id) {
        return ResponseEntity.ok(invitationsService.updateInvitationStatus(id, "CANCELLED"));
    }

    // Estado de una invitación específica
    @GetMapping("/{id}/status")
    public ResponseEntity<String> getInvitationStatus(@PathVariable UUID id) {
        return invitationsService.getInvitationById(id)
                .map(inv -> ResponseEntity.ok(inv.getStatus()))
                .orElse(ResponseEntity.notFound().build());
    }
}
