package com.lavacaapi.lavaca.transactions.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class AporteRequestDTO {
    @JsonProperty("vaca_id")
    private UUID vacaId;
    @JsonProperty("participant_id")
    private UUID participantId;
    @JsonProperty("amount")
    private int amount;
    @JsonProperty("description")
    private String description;
    @JsonProperty("type")
    private String type = "aporte";
    @JsonProperty("user_id")
    private UUID userId;

    public UUID getVacaId() {
        return vacaId;
    }

    public void setVacaId(UUID vacaId) {
        this.vacaId = vacaId;
    }

    public UUID getParticipantId() {
        return participantId;
    }

    public void setParticipantId(UUID participantId) {
        this.participantId = participantId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AporteRequestDTO{" +
                "vacaId=" + vacaId +
                ", participantId=" + participantId +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", userId=" + userId +
                '}';
    }
}
