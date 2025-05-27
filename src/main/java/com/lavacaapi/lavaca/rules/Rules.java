package com.lavacaapi.lavaca.rules;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "rules")
public class Rules {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "vaca_id", nullable = false, length = 36)
    private UUID vacaId;

    @Column(name = "rule_type", nullable = false, length = 50)
    private String ruleType;

    @Column(name = "value", length = 255)
    private String value;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public Rules() {
    }

    public Rules(UUID id, UUID vacaId, String ruleType, String value, String description, Timestamp createdAt) {
        this.id = id;
        this.vacaId = vacaId;
        this.ruleType = ruleType;
        this.value = value;
        this.description = description;
        this.createdAt = createdAt;
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

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
}
