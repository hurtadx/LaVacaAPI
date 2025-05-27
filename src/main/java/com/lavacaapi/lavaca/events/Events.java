package com.lavacaapi.lavaca.events;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "events")
public class Events {

    @Id
    @Column(length = 36, nullable = false, unique = true)
    private UUID id;

    @Column(nullable = false, length = 36, name = "vaca_id")
    private UUID vacaId;

    @Column(length = 255)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false, name = "created_at")
    private Timestamp createdAt;

    @Column(length = 36, name = "user_id")
    private UUID userId;

    public Events() {
    }

    public Events(UUID id, UUID vacaId, String title, String description, Timestamp createdAt, UUID userId) {
        this.id = id;
        this.vacaId = vacaId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.userId = userId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
