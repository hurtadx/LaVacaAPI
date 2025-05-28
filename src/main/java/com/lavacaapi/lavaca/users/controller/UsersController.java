package com.lavacaapi.lavaca.users.controller;

import com.lavacaapi.lavaca.vacas.Vacas;
import com.lavacaapi.lavaca.vacas.service.VacasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private VacasService vacasService;

    @GetMapping("/{userId}/vacas")
    public ResponseEntity<List<Vacas>> getUserVacas(@PathVariable UUID userId) {
        // Delegamos a VacasService que ya tiene esta funcionalidad
        List<Vacas> vacas = vacasService.getVacasByUserId(userId);
        return ResponseEntity.ok(vacas);
    }
}
