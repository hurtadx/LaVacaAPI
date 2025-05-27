package com.lavacaapi.lavaca.vacas.controller;

import com.lavacaapi.lavaca.vacas.Vacas;
import com.lavacaapi.lavaca.vacas.service.VacasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/vacas")
public class VacasController {

    @Autowired
    private VacasService vacasService;

    @PostMapping
    public ResponseEntity<Vacas> createVaca(@RequestBody Vacas vaca) {
        return new ResponseEntity<>(vacasService.createVaca(vaca), HttpStatus.CREATED);
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
