package Model;

import jakarta.persistence.*;

import java.security.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "vaca_id", nullable = false)
    private UUID vaca_id;

    @Column(name = "user_id", nullable = false)
    private UUID user_id;

    @Column(name = "transaction_id")
    private UUID transaction_id;

    @Column(name = "vote", nullable = false)
    private String vote;

    @Column(name = "reason")
    private String reason;

    @Column(name = "created_at", nullable = false)
    private Timestamp created_at;

    public Vote() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getVaca_id() {
        return vaca_id;
    }

    public void setVaca_id(UUID vaca_id) {
        this.vaca_id = vaca_id;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public UUID getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(UUID transaction_id) {
        this.transaction_id = transaction_id;
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

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}