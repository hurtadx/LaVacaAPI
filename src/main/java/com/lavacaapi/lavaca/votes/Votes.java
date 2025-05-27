package com.lavacaapi.lavaca.votes;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "votes")
public class Votes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "vaca_id", nullable = false)
    private UUID vacaId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "transaction_id")
    private UUID transactionId;

    @Column(name = "vote", nullable = false)
    private String vote;

    @Column(name = "reason")
    private String reason;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public Votes() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getVacaId() {
        return vacaId;
    }

    public void setVacaId(UUID vacaId) {
        this.vacaId = vacaId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}


