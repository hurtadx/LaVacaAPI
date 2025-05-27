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

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

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

