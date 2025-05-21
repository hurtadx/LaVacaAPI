package com.lavaca.lavacaapi.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "invitations")
    public class Invitation {

        @Id
        @Column(length = 36, nullable = false, unique = true)
        private UUID id;

        @Column(nullable = false, length = 36)
        private UUID vacaId;

        @Column(nullable = false, length = 36)
        private UUID senderId;

        @Column(nullable = false, length = 20)
        private String status;

        @Column(nullable = false)
        private Timestamp createdAt;

        public Invitation() {
        }

        public Invitation(Timestamp createdAt, String status, UUID senderId, UUID vacaId, UUID id) {
            this.createdAt = createdAt;
            this.status = status;
            this.senderId = senderId;
            this.vacaId = vacaId;
            this.id = id;
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

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
