package com.lavacaapi.lavaca.transactions.controller;

import com.lavacaapi.lavaca.transactions.Transactions;
import com.lavacaapi.lavaca.transactions.service.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {

    @Autowired
    private TransactionsService transactionsService;

    @PostMapping
    public ResponseEntity<Transactions> createTransaction(@RequestBody Transactions transaction) {
        return new ResponseEntity<>(transactionsService.createTransaction(transaction), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Transactions>> getAllTransactions() {
        return ResponseEntity.ok(transactionsService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transactions> getTransactionById(@PathVariable UUID id) {
        Optional<Transactions> transaction = transactionsService.getTransactionById(id);
        return transaction.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/vaca/{vacaId}")
    public ResponseEntity<List<Transactions>> getTransactionsByVaca(@PathVariable UUID vacaId) {
        return ResponseEntity.ok(transactionsService.getTransactionsByVaca(vacaId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transactions>> getTransactionsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(transactionsService.getTransactionsByUser(userId));
    }

    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<?> getUserTransactionSummary(@PathVariable UUID userId) {
        return ResponseEntity.ok(transactionsService.getUserTransactionSummary(userId));
    }

    // Transacciones pendientes de aprobación de una vaca
    @GetMapping("/vaca/{vacaId}/pending")
    public ResponseEntity<List<Transactions>> getPendingTransactionsByVaca(@PathVariable UUID vacaId) {
        return ResponseEntity.ok(transactionsService.getPendingTransactionsByVaca(vacaId));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Transactions> approveTransaction(@PathVariable UUID id, @RequestParam UUID approvedBy) {
        return ResponseEntity.ok(transactionsService.approveTransaction(id, approvedBy));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Transactions> rejectTransaction(@PathVariable UUID id, @RequestParam UUID rejectedBy) {
        return ResponseEntity.ok(transactionsService.rejectTransaction(id, rejectedBy));
    }
}
