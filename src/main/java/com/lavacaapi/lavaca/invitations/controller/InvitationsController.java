package com.lavacaapi.lavaca.invitations.controller;

import com.lavacaapi.lavaca.invitations.Invitations;
import com.lavacaapi.lavaca.invitations.service.InvitationsService;
import com.lavacaapi.lavaca.participants.Participants;
import com.lavacaapi.lavaca.participants.service.ParticipantsService;
import com.lavacaapi.lavaca.profiles.repository.ProfilesRepository;
import com.lavacaapi.lavaca.profiles.Profiles;
import com.lavacaapi.lavaca.vacas.repository.VacasRepository;
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

    @Autowired
    private ParticipantsService participantsService;

    @Autowired
    private ProfilesRepository profilesRepository;

    @Autowired
    private VacasRepository vacasRepository;

    @PostMapping
    public ResponseEntity<Invitations> createInvitation(@RequestBody Invitations invitation) {
        return new ResponseEntity<>(invitationsService.createInvitation(invitation), HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> createInvitationsBulk(@RequestBody BulkInvitationRequest request) {
        if (request.getUserIds() == null || request.getUserIds().isEmpty()) {
            return ResponseEntity.badRequest().body("user_ids es obligatorio");
        }
        if (request.getVacaId() == null || request.getSenderId() == null) {
            return ResponseEntity.badRequest().body("vaca_id y sender_id son obligatorios");
        }
        // Buscar los usuarios que ya están en la vaca
        List<String> yaParticipan = request.getUserIds().stream()
            .filter(userId -> participantsService.existsByVacaIdAndUserId(request.getVacaId(), userId))
            .map(userId -> profilesRepository.findByUserId(userId)
                .map(Profiles::getUsername)
                .orElse(userId.toString()))
            .toList();
        if (!yaParticipan.isEmpty()) {
            return ResponseEntity.badRequest().body("El usuario(s) " + String.join(", ", yaParticipan) + " ya se encuentra(n) en la vaca");
        }
        // Obtener datos para el mensaje personalizado
        String senderName = profilesRepository.findByUserId(request.getSenderId())
            .map(Profiles::getUsername)
            .orElse("Alguien");
        String vacaName = vacasRepository.findById(request.getVacaId())
            .map(v -> v.getName())
            .orElse("una vaca");
        // Crear invitaciones y armar la respuesta personalizada
        List<BulkInvitationResponse.InvitationInfo> invitationInfos = request.getUserIds().stream()
            .map(userId -> {
                Invitations invitation = new Invitations();
                invitation.setVacaId(request.getVacaId());
                invitation.setSenderId(request.getSenderId());
                invitation.setReceiverId(userId);
                invitation.setStatus("pending");
                invitation.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                invitation.setId(java.util.UUID.randomUUID());
                invitationsService.createInvitation(invitation);
                String username = profilesRepository.findByUserId(userId)
                    .map(Profiles::getUsername)
                    .orElse(userId.toString());
                String message = String.format("El usuario %s te ha invitado a la vaca %s", senderName, vacaName);
                return new BulkInvitationResponse.InvitationInfo(userId, username, message);
            })
            .toList();
        BulkInvitationResponse.SenderInfo senderInfo = new BulkInvitationResponse.SenderInfo(request.getSenderId(), senderName);
        BulkInvitationResponse.VacaInfo vacaInfo = new BulkInvitationResponse.VacaInfo(request.getVacaId(), vacaName);
        BulkInvitationResponse response = new BulkInvitationResponse(invitationInfos, senderInfo, vacaInfo);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
        Invitations invitation = invitationsService.updateInvitationStatus(id, status);
        // Agregar como participante si el nuevo estado es ACCEPTED
        if (invitation != null && "ACCEPTED".equalsIgnoreCase(invitation.getStatus())) {
            Participants participant = new Participants();
            participant.setVacaId(invitation.getVacaId());
            participant.setUserId(invitation.getReceiverId());
            participant.setStatus("activo");
            participantsService.createParticipant(participant);
        }
        return ResponseEntity.ok(invitation);
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
    public ResponseEntity<InvitationActionResponse> acceptInvitation(@PathVariable UUID id) {
        Invitations invitation = invitationsService.updateInvitationStatus(id, "ACCEPTED");
        if (invitation != null && "ACCEPTED".equalsIgnoreCase(invitation.getStatus())) {
            Participants participant = new Participants();
            participant.setVacaId(invitation.getVacaId());
            participant.setUserId(invitation.getReceiverId());
            participant.setStatus("activo");
            // Buscar el nombre del usuario en Profiles
            String name = profilesRepository.findByUserId(invitation.getReceiverId())
                .map(Profiles::getUsername)
                .orElse("Sin nombre");
            participant.setName(name);
            participantsService.createParticipant(participant);
        }
        // Obtener datos de la vaca y del sender
        String vacaName = vacasRepository.findById(invitation.getVacaId())
            .map(v -> v.getName())
            .orElse("Vaca desconocida");
        String senderUsername = profilesRepository.findByUserId(invitation.getSenderId())
            .map(Profiles::getUsername)
            .orElse("Usuario desconocido");
        InvitationActionResponse response = new InvitationActionResponse(
            invitation.getId(),
            invitation.getStatus(),
            invitation.getVacaId(),
            vacaName,
            invitation.getSenderId(),
            senderUsername
        );
        return ResponseEntity.ok(response);
    }

    // Rechazar invitación
    @PutMapping("/{id}/reject")
    public ResponseEntity<InvitationActionResponse> rejectInvitation(@PathVariable UUID id) {
        Invitations invitation = invitationsService.updateInvitationStatus(id, "REJECTED");
        // Obtener datos de la vaca y del sender
        String vacaName = vacasRepository.findById(invitation.getVacaId())
            .map(v -> v.getName())
            .orElse("Vaca desconocida");
        String senderUsername = profilesRepository.findByUserId(invitation.getSenderId())
            .map(Profiles::getUsername)
            .orElse("Usuario desconocido");
        InvitationActionResponse response = new InvitationActionResponse(
            invitation.getId(),
            invitation.getStatus(),
            invitation.getVacaId(),
            vacaName,
            invitation.getSenderId(),
            senderUsername
        );
        return ResponseEntity.ok(response);
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

