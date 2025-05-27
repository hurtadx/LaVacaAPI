package com.lavacaapi.lavaca.participants;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "participants")
public class Participants {

    @Id
    @Column(length = 36, nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false, length = 36)
    private UUID vacaId;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private Timestamp createdAt;

    @Column(length = 36)
    private UUID userId;

    @Column(nullable = false)
    private String status = "pendiente";  // Valores: activo, pendiente, inactivo


    public Participants() {
    }


    public Participants(UUID id, UUID vacaId, String name, String email, Timestamp createdAt, UUID userId, String status) {
        this.id = id;
        this.vacaId = vacaId;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.userId = userId;
        this.status = status;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
