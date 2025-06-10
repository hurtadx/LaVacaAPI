package com.lavacaapi.lavaca.transactions.controller;

import com.lavacaapi.lavaca.transactions.Transactions;
import com.lavacaapi.lavaca.transactions.service.TransactionsService;
import com.lavacaapi.lavaca.transactions.dto.AporteRequestDTO;
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

    @PostMapping("/aporte")
    public ResponseEntity<Transactions> createAporte(@RequestBody AporteRequestDTO aporteRequestDTO) {
        Transactions created = transactionsService.createAporte(aporteRequestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

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
}

