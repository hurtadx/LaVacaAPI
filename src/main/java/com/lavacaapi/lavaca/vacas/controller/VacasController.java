package com.lavacaapi.lavaca.vacas.controller;

import com.lavacaapi.lavaca.vacas.Vacas;
import com.lavacaapi.lavaca.vacas.service.VacasService;
import com.lavacaapi.lavaca.participants.Participants;
import com.lavacaapi.lavaca.participants.service.ParticipantsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/vacas")
public class VacasController {

    @Autowired
    private VacasService vacasService;

    @Autowired
    private ParticipantsService participantsService;

    @PostMapping
    public ResponseEntity<Vacas> createVaca(@RequestBody Vacas vaca) {
        try {
            // Validaciones existentes
            if (vaca.getName() == null || vaca.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (vaca.getGoal() == null) {
                return ResponseEntity.badRequest().build();
            }
            if (vaca.getUserId() == null) {
                return ResponseEntity.badRequest().build();
            }

            // Generar ID y valores por defecto
            if (vaca.getId() == null) {
                vaca.setId(UUID.randomUUID());
            }
            if (vaca.getCurrent() == null) {
                vaca.setCurrent(BigDecimal.ZERO);
            }
            if (vaca.getCreatedAt() == null) {
                vaca.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            }
            if (vaca.getIsActive() == null) {
                vaca.setIsActive(true);
            }
            if (vaca.getStatus() == null || "ACTIVE".equals(vaca.getStatus())) {
                vaca.setStatus("active");
            }

            // Crear la vaca
            Vacas createdVaca = vacasService.createVaca(vaca);

            // Agregar al usuario creador como participante
            Participants creatorParticipant = new Participants();
            creatorParticipant.setVacaId(createdVaca.getId());
            creatorParticipant.setUserId(vaca.getUserId());
            creatorParticipant.setStatus("activo");
            participantsService.createParticipant(creatorParticipant);

            return new ResponseEntity<>(createdVaca, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Vacas>> getAllVacas() {
        return ResponseEntity.ok(vacasService.getAllVacas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vacas> getVacaById(@PathVariable UUID id) {
        Optional<Vacas> vaca = vacasService.getVacaById(id);
        return vaca.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public ResponseEntity<List<Vacas>> getActiveVacas() {
        return ResponseEntity.ok(vacasService.getVacasByIsActive(true));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Vacas>> getVacasByStatus(@PathVariable String status) {
        return ResponseEntity.ok(vacasService.getVacasByStatus(status));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Vacas>> getVacasByUserId(@PathVariable UUID userId) {
        List<Vacas> vacas = vacasService.getVacasByUserId(userId);
        return ResponseEntity.ok(vacas);
    }

    @GetMapping("/{vacaId}/summary")
    public ResponseEntity<?> getVacaSummary(@PathVariable UUID vacaId) {
        return ResponseEntity.ok(vacasService.getVacaSummary(vacaId));
    }

    @GetMapping("/{vacaId}/progress")
    public ResponseEntity<?> getVacaProgress(@PathVariable UUID vacaId) {
        return ResponseEntity.ok(vacasService.getVacaProgress(vacaId));
    }

    @GetMapping("/{vacaId}/timeline")
    public ResponseEntity<?> getVacaTimeline(@PathVariable UUID vacaId) {
        return ResponseEntity.ok(vacasService.getVacaTimeline(vacaId));
    }

    @PostMapping("/{vacaId}/join")
    public ResponseEntity<?> joinVaca(@PathVariable UUID vacaId, @RequestParam UUID userId) {
        vacasService.joinVaca(vacaId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{vacaId}/leave")
    public ResponseEntity<?> leaveVaca(@PathVariable UUID vacaId, @RequestParam UUID userId) {
        vacasService.leaveVaca(vacaId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{vacaId}/status")
    public ResponseEntity<?> updateVacaStatus(@PathVariable UUID vacaId, @RequestParam String status) {
        vacasService.updateVacaStatus(vacaId, status);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vacas> updateVaca(@PathVariable UUID id, @RequestBody Vacas vaca) {
        return ResponseEntity.ok(vacasService.updateVaca(id, vaca));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVaca(@PathVariable UUID id) {
        vacasService.deleteVaca(id);
        return ResponseEntity.noContent().build();
    }
}
