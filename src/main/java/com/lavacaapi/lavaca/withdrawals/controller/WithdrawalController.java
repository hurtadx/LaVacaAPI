package com.lavacaapi.lavaca.withdrawals.controller;

import com.lavacaapi.lavaca.withdrawals.Withdrawal;
import com.lavacaapi.lavaca.withdrawals.dto.WithdrawalResultDTO;
import com.lavacaapi.lavaca.withdrawals.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/withdrawals")
public class WithdrawalController {
    @Autowired
    private WithdrawalService withdrawalService;

    @PostMapping
    public ResponseEntity<Withdrawal> createWithdrawal(@RequestBody Withdrawal withdrawal) {
        Withdrawal created = withdrawalService.createWithdrawal(withdrawal);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/vaca/{vacaId}")
    public ResponseEntity<List<Withdrawal>> getWithdrawalsByVaca(@PathVariable UUID vacaId) {
        return ResponseEntity.ok(withdrawalService.getWithdrawalsByVacaId(vacaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Withdrawal> getWithdrawalById(@PathVariable UUID id) {
        Optional<Withdrawal> withdrawal = withdrawalService.getWithdrawalById(id);
        return withdrawal.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/result")
    public ResponseEntity<WithdrawalResultDTO> getWithdrawalResult(@PathVariable UUID id) {
        WithdrawalResultDTO result = withdrawalService.getWithdrawalResult(id);
        return ResponseEntity.ok(result);
    }
}
