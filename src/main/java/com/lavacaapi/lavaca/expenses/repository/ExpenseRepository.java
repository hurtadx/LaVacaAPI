package com.lavacaapi.lavaca.expenses.repository;

import com.lavacaapi.lavaca.expenses.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    List<Expense> findByVacaId(UUID vacaId);
    List<Expense> findByUserId(UUID userId);
}

