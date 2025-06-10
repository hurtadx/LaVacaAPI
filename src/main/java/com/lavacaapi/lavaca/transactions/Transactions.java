package com.lavacaapi.lavaca.transactions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transactions {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "vaca_id", nullable = false)
    private UUID vacaId;

    @Column(name = "participant_id", nullable = false)
    private UUID participantId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "approved_by")
    private UUID approvedBy;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "vacaid", nullable = false)
    private UUID vacaid;

    @Column(name = "participantid", nullable = false)
    private UUID participantid;

    public Transactions() {
    }

    public Transactions(UUID id, UUID vacaId, UUID participantId, int amount, String description, Timestamp date, UUID userId, String type, UUID approvedBy, String paymentMethod) {
        this.id = id;
        this.vacaId = vacaId;
        this.participantId = participantId;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.userId = userId;
        this.type = type;
        this.approvedBy = approvedBy;
        this.paymentMethod = paymentMethod;
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

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(UUID approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public UUID getVacaid() {
        return vacaid;
    }

    public void setVacaid(UUID vacaid) {
        this.vacaid = vacaid;
    }

    public UUID getParticipantid() {
        return participantid;
    }

    public void setParticipantid(UUID participantid) {
        this.participantid = participantid;
    }
}
