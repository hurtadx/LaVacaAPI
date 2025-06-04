package com.lavacaapi.lavaca.participants.controller;

import com.lavacaapi.lavaca.participants.Participants;
import com.lavacaapi.lavaca.participants.service.ParticipantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/participants")
public class ParticipantsController {

    @Autowired
    private ParticipantsService participantsService;

    @PostMapping
    public ResponseEntity<Participants> createParticipant(@RequestBody Participants participant) {
        return new ResponseEntity<>(participantsService.createParticipant(participant), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Participants>> getAllParticipants() {
        return ResponseEntity.ok(participantsService.getAllParticipants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participants> getParticipantById(@PathVariable UUID id) {
        return participantsService.getParticipantById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/vaca/{vacaId}")
    public ResponseEntity<List<Participants>> getParticipantsByVaca(@PathVariable UUID vacaId) {
        return ResponseEntity.ok(participantsService.getParticipantsByVacaId(vacaId));
    }

    @GetMapping("/vaca/{vacaId}/active")
    public ResponseEntity<List<Participants>> getActiveParticipantsByVaca(@PathVariable UUID vacaId) {
        return ResponseEntity.ok(participantsService.getActiveParticipantsByVacaId(vacaId));
    }

    @GetMapping("/vaca/{vacaId}/pending")
    public ResponseEntity<List<Participants>> getPendingParticipantsByVaca(@PathVariable UUID vacaId) {
        return ResponseEntity.ok(participantsService.getPendingParticipantsByVacaId(vacaId));
    }

    @GetMapping("/vaca/{vacaId}/pageable")
    public ResponseEntity<Page<Participants>> getParticipantsByVacaPageable(
            @PathVariable UUID vacaId,
            Pageable pageable) {
        return ResponseEntity.ok(participantsService.getParticipantsByVacaIdPageable(vacaId, pageable));
    }

    @GetMapping("/vaca/{vacaId}/details")
    public ResponseEntity<List<Map<String, Object>>> getParticipantsByVacaWithDetails(@PathVariable UUID vacaId) {
        return ResponseEntity.ok(participantsService.getParticipantsByVacaWithDetails(vacaId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Participants>> getParticipantsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(participantsService.getParticipantsByUserId(userId));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<Participants>> getParticipantsByEmail(@PathVariable String email) {
        return ResponseEntity.ok(participantsService.getParticipantsByEmail(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participants> updateParticipant(@PathVariable UUID id, @RequestBody Participants participant) {
        return ResponseEntity.ok(participantsService.updateParticipant(id, participant));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Participants> updateParticipantStatus(
            @PathVariable UUID id,
            @RequestBody String status) {
        return ResponseEntity.ok(participantsService.updateParticipantStatus(id, status));
    }

    @PostMapping("/{participantId}/activate")
    public ResponseEntity<Participants> activateParticipant(@PathVariable UUID participantId) {
        return ResponseEntity.ok(participantsService.updateParticipantStatus(participantId, "activo"));
    }

    @PostMapping("/{participantId}/deactivate")
    public ResponseEntity<Participants> deactivateParticipant(@PathVariable UUID participantId) {
        return ResponseEntity.ok(participantsService.updateParticipantStatus(participantId, "inactivo"));
    }

    @GetMapping("/{participantId}/contributions")
    public ResponseEntity<?> getParticipantContributions(@PathVariable UUID participantId) {
        return ResponseEntity.ok(participantsService.getParticipantContributions(participantId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable UUID id) {
        participantsService.deleteParticipant(id);
        return ResponseEntity.noContent().build();
    }

    // Bulk invite de participantes
    @PostMapping("/bulk-invite")
    public ResponseEntity<List<Participants>> bulkInviteParticipants(@RequestBody List<Participants> participants) {
        return new ResponseEntity<>(participantsService.bulkInviteParticipants(participants), HttpStatus.CREATED);
    }
}
