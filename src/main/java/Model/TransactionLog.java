package Model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "transaction_logs")
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "transaction_id", nullable = false)
    private UUID transaction_id;

    @Column(name = "user_id", nullable = false)
    private UUID user_id;

    @Column(name = "action", nullable = false)
    private String action;

   //los datos son jsonb pero se pueden usar con String
    @Column(name = "old_data", columnDefinition = "jsonb")
    private String old_data;

    @Column(name = "new_data", columnDefinition = "jsonb")
    private String new_data;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    public TransactionLog() {
    }

    public TransactionLog(UUID id, UUID transaction_id, UUID user_id, String action, String old_data, String new_data, Timestamp timestamp) {
        this.id = id;
        this.transaction_id = transaction_id;
        this.user_id = user_id;
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

    public UUID getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(UUID transaction_id) {
        this.transaction_id = transaction_id;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
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


