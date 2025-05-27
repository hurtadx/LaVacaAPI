package com.lavacaapi.lavaca.expenses.service;

import com.lavacaapi.lavaca.expenses.Expense;
import com.lavacaapi.lavaca.expenses.repository.ExpenseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    // Métodos de recibos de gastos (simples, solo para demo, sin persistencia real)
    private final Map<UUID, List<String>> expenseReceipts = new HashMap<>();

    public List<String> getExpenseReceipts(UUID id) {
        return expenseReceipts.getOrDefault(id, Collections.emptyList());
    }

    public List<String> addExpenseReceipt(UUID id, String receiptInfo) {
        expenseReceipts.computeIfAbsent(id, k -> new ArrayList<>()).add(receiptInfo);
        return expenseReceipts.get(id);
    }

    @Transactional
    public Expense createExpense(Expense expense) {
        if (expense.getId() == null) {
            expense.setId(UUID.randomUUID());
        }
        if (expense.getCreatedAt() == null) {
            expense.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }
        if (expense.getStatus() == null) {
            expense.setStatus("PENDING");
        }
        return expenseRepository.save(expense);
    }

    public List<Expense> getExpensesByVacaId(UUID vacaId) {
        return expenseRepository.findByVacaId(vacaId);
    }

    public List<Expense> getExpensesByUserId(UUID userId) {
        return expenseRepository.findByUserId(userId);
    }

    public Optional<Expense> getExpenseById(UUID id) {
        return expenseRepository.findById(id);
    }

    @Transactional
    public Expense approveExpense(UUID id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el gasto con ID: " + id));
        expense.setStatus("APPROVED");
        return expenseRepository.save(expense);
    }

    @Transactional
    public Expense rejectExpense(UUID id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el gasto con ID: " + id));
        expense.setStatus("REJECTED");
        return expenseRepository.save(expense);
    }
}

