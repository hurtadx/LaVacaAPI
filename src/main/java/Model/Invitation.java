package Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
    public class Invitation {

        @Id
        @Column(length = 36, nullable = false, unique = true)
        private UUID id;

        @Column(nullable = false, length = 36)
        private String vaca_id;

        @Column(nullable = false, length = 36)
        private String sender_id;

        @Column(nullable = false, length = 20)
        private String status;

        @Column(nullable = false)
        private Timestamp created_at;

        public Invitation() {
        }

        public Invitation(Timestamp created_at, String status, String sender_id, String vaca_id, UUID id) {
            this.created_at = created_at;
            this.status = status;
            this.sender_id = sender_id;
            this.vaca_id = vaca_id;
            this.id = id;
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

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
