package com.lavacaapi.lavaca.transaction_logs;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "transaction_logs")
public class TransactionLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "transaction_id", nullable = false)
    private UUID transactionId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "action", nullable = false)
    private String action;

    //los datos son jsonb pero se pueden usar con String
    @Column(name = "old_data", columnDefinition = "jsonb")
    private String old_data;

    @Column(name = "new_data", columnDefinition = "jsonb")
    private String new_data;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    public TransactionLogs() {
    }

    public TransactionLogs(UUID id, UUID transactionId, UUID userId, String action, String old_data, String new_data, Timestamp timestamp) {
        this.id = id;
        this.transactionId = transactionId;
        this.userId = userId;
        this.action = action;
        this.old_data = old_data;
        this.new_data = new_data;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOld_data() {
        return old_data;
    }

    public void setOld_data(String old_data) {
        this.old_data = old_data;
    }

    public String getNew_data() {
        return new_data;
    }

    public void setNew_data(String new_data) {
        this.new_data = new_data;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

