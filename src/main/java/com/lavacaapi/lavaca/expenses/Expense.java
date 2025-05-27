package com.lavacaapi.lavaca.expenses;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "vaca_id", nullable = false)
    private UUID vacaId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "status", length = 20)
    private String status; // PENDING, APPROVED, REJECTED

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public Expense() {}

    public Expense(UUID id, UUID vacaId, UUID userId, BigDecimal amount, String description, String status, Timestamp createdAt) {
        this.id = id;
        this.vacaId = vacaId;
        this.userId = userId;
        this.amount = amount;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getVacaId() { return vacaId; }
    public void setVacaId(UUID vacaId) { this.vacaId = vacaId; }
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}

