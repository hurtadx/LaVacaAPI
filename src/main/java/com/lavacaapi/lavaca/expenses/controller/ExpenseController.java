package com.lavacaapi.lavaca.expenses.controller;

import com.lavacaapi.lavaca.expenses.Expense;
import com.lavacaapi.lavaca.expenses.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expense) {
        return new ResponseEntity<>(expenseService.createExpense(expense), HttpStatus.CREATED);
    }

    @GetMapping("/vaca/{vacaId}")
    public ResponseEntity<List<Expense>> getExpensesByVaca(@PathVariable UUID vacaId) {
        return ResponseEntity.ok(expenseService.getExpensesByVacaId(vacaId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Expense>> getExpensesByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(expenseService.getExpensesByUserId(userId));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Expense> approveExpense(@PathVariable UUID id) {
        return ResponseEntity.ok(expenseService.approveExpense(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Expense> rejectExpense(@PathVariable UUID id) {
        return ResponseEntity.ok(expenseService.rejectExpense(id));
    }

    // Obtener recibos de un gasto
    @GetMapping("/{id}/receipts")
    public ResponseEntity<List<String>> getExpenseReceipts(@PathVariable UUID id) {
        return ResponseEntity.ok(expenseService.getExpenseReceipts(id));
    }

    // Agregar recibo a un gasto (solo información textual)
    @PostMapping("/{id}/receipts")
    public ResponseEntity<List<String>> addExpenseReceipt(@PathVariable UUID id, @RequestBody String receiptInfo) {
        return ResponseEntity.ok(expenseService.addExpenseReceipt(id, receiptInfo));
    }

    // Endpoints para receipts se implementarán después
}

