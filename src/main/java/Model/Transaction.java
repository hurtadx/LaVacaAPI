package Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.util.UUID;
@Entity
public class Transaction {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "vaca_id", nullable = false, length = 36)
    private String vaca_id;

    @Column(name = "participant_id", nullable = false)
    private UUID participant_id;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "date", nullable = false)
    private Timestamp date;

    @Column(name = "user_id", nullable = false)
    private UUID user_id;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "approved_by")
    private UUID approved_by;

    @Column(name = "payment_method", length = 50)
    private String payment_method;

    public Transaction() {
    }

    public Transaction(UUID id, String vaca_id, UUID participant_id, int amount, String description, Timestamp date, UUID user_id, String type, UUID approved_by, String payment_method) {
        this.id = id;
        this.vaca_id = vaca_id;
        this.participant_id = participant_id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.user_id = user_id;
        this.type = type;
        this.approved_by = approved_by;
        this.payment_method = payment_method;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getVaca_id() {
        return vaca_id;
    }

    public void setVaca_id(String vaca_id) {
        this.vaca_id = vaca_id;
    }

    public UUID getParticipant_id() {
        return participant_id;
    }

    public void setParticipant_id(UUID participant_id) {
        this.participant_id = participant_id;
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

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(UUID approved_by) {
        this.approved_by = approved_by;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }
}
