package com.lavaca.lavacaapi.dto;

import java.util.UUID;

public class TransactionDTO {
    private UUID vacaId;
    private UUID participantId;
    private int amount;
    private String description;
    private UUID userId;
    private String type; // "DEPOSIT", "WITHDRAWAL"
    private String paymentMethod;

    // Getters y setters
    public UUID getVacaId() { return vacaId; }
    public void setVacaId(UUID vacaId) { this.vacaId = vacaId; }

    public UUID getParticipantId() { return participantId; }
    public void setParticipantId(UUID participantId) { this.participantId = participantId; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
